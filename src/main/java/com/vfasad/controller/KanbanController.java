package com.vfasad.controller;

import com.google.gson.Gson;
import com.vfasad.dto.Order;
import com.vfasad.dto.OrderConsume;
import com.vfasad.dto.Product;
import com.vfasad.dto.ProductAction;
import com.vfasad.repo.OrderRepository;
import com.vfasad.repo.ProductActionRepository;
import com.vfasad.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.function.Function;

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
    public ModelAndView kanbanBoard() {
        ModelAndView model = new ModelAndView("order/kanban-dashboard");
        model.addObject("createdOrders", orderRepository.findByStatus(Order.Status.CREATED));
        model.addObject("inProgressOrders", orderRepository.findByStatus(Order.Status.IN_PROGRESS));
        model.addObject("completedOrders", orderRepository.findByStatus(Order.Status.COMPLETED));
        model.addObject("ordersJson", gson.toJson(orderRepository.findAll().stream().collect(toMap(Order::getId, Function.identity()))));
        return model;
    }

    @RequestMapping(value = "/kanban", method = RequestMethod.POST)
    public String moveOrder(
            @RequestParam Long orderId,
            @RequestParam(required = false) long[] productIds,
            @RequestParam(required = false) int[] actualQuantities) {
        Order order = orderRepository.findOne(orderId);
        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            for (int i = 0; i < productIds.length; i++) {
                Product product = productRepository.findOne(productIds[i]);
                OrderConsume consume = getConsumeByProductId(order, productIds[i]);
                consume.setActualUsedQuantity(actualQuantities[i]);
                int remain = consume.getCalculatedQuantity() - actualQuantities[i];
                productActionRepository.save(new ProductAction(
                        remain,
                        0,
                        ProductAction.Type.RETURN,
                        product,
                        null // todo: fill from creds
                ));
                product.setQuantity(product.getQuantity() + remain);
                productRepository.save(product);
            }
            order.setStatus(Order.Status.COMPLETED);
        } else if (order.getStatus() == Order.Status.CREATED) {
            order.getConsumes().forEach(c -> {
                productActionRepository.save(new ProductAction(
                        c.getCalculatedQuantity(),
                        0,
                        ProductAction.Type.SPEND,
                        c.getProduct(),
                        null // todo: fill from creds
                ));
                c.getProduct().setQuantity(c.getProduct().getQuantity() - c.getCalculatedQuantity());
                productRepository.save(c.getProduct());
            });
            order.setStatus(Order.Status.IN_PROGRESS);
        } else if (order.getStatus() == Order.Status.COMPLETED) {
            order.setStatus(Order.Status.CLOSED);
        }
        orderRepository.save(order);
        return "redirect:/kanban";
    }

    private OrderConsume getConsumeByProductId(Order order, long productId) {
        return order.getConsumes().stream().filter(c -> c.getProduct().getId() == productId).findAny().get();
    }
}
