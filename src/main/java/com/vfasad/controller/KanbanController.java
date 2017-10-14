package com.vfasad.controller;

import com.google.gson.Gson;
import com.vfasad.entity.Order;
import com.vfasad.entity.OrderConsume;
import com.vfasad.entity.Product;
import com.vfasad.entity.ProductAction;
import com.vfasad.repo.OrderRepository;
import com.vfasad.repo.ProductActionRepository;
import com.vfasad.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

import static com.vfasad.entity.User.*;
import static java.util.stream.Collectors.toMap;

@Controller
public class KanbanController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductActionRepository productActionRepository;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/kanban", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER, ROLE_SALES})
    public ModelAndView kanbanBoard() {
        ModelAndView model = new ModelAndView("order/kanban-dashboard");
        List<Order> createdOrders =  orderRepository.findByStatusIn(Order.Status.CREATED);
        List<BigInteger> blockedIds = orderRepository.findBlockedOrdersIds();
        createdOrders.forEach(o -> {
            if (blockedIds.contains(new BigInteger(String.valueOf(o.getId())))) o.setStatus(Order.Status.BLOCKED);
        });
        model.addObject("createdOrders",createdOrders);
        model.addObject("inProgressOrders", orderRepository.findByStatusIn(Order.Status.IN_PROGRESS));
        model.addObject("shippingOrders", orderRepository.findByStatusIn(Order.Status.SHIPPING));
        model.addObject("ordersJson", gson.toJson(
                orderRepository.findByStatusIn(Order.Status.CREATED, Order.Status.IN_PROGRESS, Order.Status.SHIPPING).stream()
                        .collect(toMap(Order::getId, Function.identity()))));
        return model;
    }

    @RequestMapping(value = "/kanban", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER})
    public String moveOrder(
            @RequestParam Long orderId,
            @RequestParam(required = false) long[] productIds,
            @RequestParam(required = false) int[] actualQuantities,
            Authentication authentication) {
        Order order = orderRepository.findOne(orderId);
        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            for (int i = 0; i < productIds.length; i++) {
                Product product = productRepository.findOne(productIds[i]);
                OrderConsume consume = getConsumeByProductId(order, productIds[i]);
                consume.setActualUsedQuantity(actualQuantities[i]);
                int remain = consume.getCalculatedQuantity() - actualQuantities[i];
                productActionRepository.save(ProductAction.createReturnAction(
                        remain,
                        product,
                        null, // todo: fill from creds
                        order
                ));
                product.setQuantity(product.getQuantity() + remain);
                productRepository.save(product);
            }
            order.setStatus(Order.Status.SHIPPING);
        } else if (order.getStatus() == Order.Status.CREATED) {
            order.getConsumes().forEach(c -> {
                productActionRepository.save(ProductAction.createSpendAction(
                        c.getCalculatedQuantity(),
                        c.getProduct(),
                        null, // todo: fill from creds
                        order
                ));
                c.getProduct().setQuantity(c.getProduct().getQuantity() - c.getCalculatedQuantity());
                productRepository.save(c.getProduct());
            });
            order.setStatus(Order.Status.IN_PROGRESS);
        } else if (order.getStatus() == Order.Status.SHIPPING) {
            order.setStatus(Order.Status.CLOSED);
        }
        orderRepository.save(order);
        return "redirect:/kanban";
    }

    private OrderConsume getConsumeByProductId(Order order, long productId) {
        return order.getConsumes().stream().filter(c -> c.getProduct().getId() == productId).findAny().get();
    }
}
