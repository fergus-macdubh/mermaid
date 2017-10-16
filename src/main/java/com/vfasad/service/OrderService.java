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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    public List<Order> findAll() {
        return orderRepository.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "created")));
    }

    public void addOrder(double area, String client, double price, Set<OrderConsume> consumes, User manager) {
        orderRepository.save(new Order(
                manager,
                area,
                client,
                price,
                consumes
        ));
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order with provided id is not found"));
    }

    public void updateOrder(Long id, double area, String client, double price, Set<OrderConsume> consumes, User manager) {
        Order order = getOrder(id);
        order.setArea(area);
        order.setClient(client);
        order.setPrice(price);
        order.setConsumes(consumes);
        order.setManager(manager);
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

    public void moveOrderInProgress(Order order) {
        order.getConsumes().forEach(c ->
            productService.spend(c.getProduct(), c.getCalculatedQuantity(), order)
        );
        order.setStatus(Order.Status.IN_PROGRESS);
        orderRepository.save(order);
    }

    public void moveOrderToShipping(Order order, long[] consumeIds, List<Double> actualQuantities) {
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
        orderRepository.save(order);
    }

    public void closeOrder(Order order) {
        order.setStatus(Order.Status.CLOSED);
        orderRepository.save(order);
    }
}
