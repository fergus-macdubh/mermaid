package com.vfasad.service;

import com.vfasad.entity.Order;
import com.vfasad.entity.OrderConsume;
import com.vfasad.entity.Product;
import com.vfasad.entity.User;
import com.vfasad.exception.NotFoundException;
import com.vfasad.repo.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
public class OrderService {
    public static final int PLANNED_COMPLETION_DATE = 7;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TeamService teamService;

    public List<Order> findAll() {
        return orderRepository.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "created")));
    }

    public void addOrder(double area, int clipCount, int furnitureSmallCount, int furnitureBigCount, String document, double price, Set<OrderConsume> consumes, User manager) {
        orderRepository.save(new Order(
                manager,
                area,
                clipCount,
                furnitureSmallCount,
                furnitureBigCount,
                document,
                price,
                consumes,
                LocalDate.now().plusDays(PLANNED_COMPLETION_DATE)
        ));
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order with provided id is not found"));
    }

    public void updateOrder(Long id, double area, int clipCount, int furnitureSmallCount, int furnitureBigCount, String document, double price, Set<OrderConsume> consumes, User manager) {
        Order order = getOrder(id);
        order.setArea(area);
        order.setDocument(document);
        order.setPrice(price);
        order.setConsumes(consumes);
        order.setManager(manager);
        order.setClipCount(clipCount);
        order.setFurnitureSmallCount(furnitureSmallCount);
        order.setFurnitureBigCount(furnitureBigCount);
        orderRepository.save(order);
    }

    public List<Order> getActiveOrders() {
        List<Order> orders = orderRepository.findByStatusIn(Order.Status.CREATED, Order.Status.IN_PROGRESS, Order.Status.SHIPPING);
        List<BigInteger> blockedIds = orderRepository.findBlockedOrdersIds();
        orders.forEach(o -> {
            if (blockedIds.contains(new BigInteger(String.valueOf(o.getId())))) o.setStatus(Order.Status.BLOCKED);
        });
        return orders;
    }

    public void moveOrderInProgress(Order order, long teamId, URL url) throws MalformedURLException {
        order.getConsumes().forEach(c ->
                productService.spend(c.getProduct(), c.getCalculatedQuantity(), order)
        );
        order.setStatus(Order.Status.IN_PROGRESS);
        order.setTeam(teamService.getTeam(teamId));
        order.setDoneBy(teamService.getTeamUsers(teamId));
        orderRepository.save(order);

        emailService.notifyManagerOrderInProgress(order, url);
    }

    public void moveOrderToShipping(Order order, long[] consumeIds, List<Double> actualQuantities, URL url) throws MalformedURLException {
        Map<Long, OrderConsume> consumeMap = order.getConsumes().stream().collect(toMap(OrderConsume::getId, Function.identity()));

        for (int i = 0; i < consumeIds.length; i++) {
            OrderConsume consume = consumeMap.get(consumeIds[i]);
            if (consume == null) throw new NotFoundException("Order consume with provided id is not found.");
            Product product = consume.getProduct();
            consume.setActualUsedQuantity(actualQuantities.get(i));
            double remain = consume.getCalculatedQuantity() - actualQuantities.get(i);
            productService.returnProduct(product, remain, order);
        }
        order.setStatus(Order.Status.SHIPPING);
        order.setCompleted(now());
        orderRepository.save(order);

        emailService.notifyManagerOrderCompleted(order, url);
    }

    public void closeOrder(Order order) {
        order.setStatus(Order.Status.CLOSED);
        orderRepository.save(order);
    }

    public void cancelOrder(Long id) {
        Order order = getOrder(id);

        if (order.getStatus() == Order.Status.SHIPPING
                || order.getStatus() == Order.Status.CLOSED
                || order.getStatus() == Order.Status.CANCELLED) {
            throw new IllegalStateException("Unable to cancel order in status [" + order.getStatus() + "]");
        }

        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            order.setStatus(Order.Status.CREATED);
            order.getConsumes().forEach(
                    consume -> productService.returnProduct(
                            consume.getProduct(),
                            consume.getCalculatedQuantity(),
                            order));
            order.setTeam(null);
            order.setDoneBy(Collections.emptyList());
        } else {
            order.setStatus(Order.Status.CANCELLED);
        }

        orderRepository.save(order);
    }

    public List<Order> findByMonth(int year, int month) {
        return orderRepository.findByCompletedBetween(
                LocalDateTime.of(year, month, 1, 0, 0),
                LocalDateTime.of(year, month, YearMonth.of(year, month).lengthOfMonth(), 23, 59, 59));
    }
}
