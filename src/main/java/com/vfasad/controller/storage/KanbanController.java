package com.vfasad.controller.storage;

import com.vfasad.dto.Order;
import com.vfasad.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class KanbanController {
    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping(value = "/kanban", method = RequestMethod.GET)
    public ModelAndView kanbanBoard() {
        ModelAndView model = new ModelAndView("order/kanban-dashboard");
        model.addObject("createdOrders", orderRepository.findByStatus(Order.Status.CREATED));
        model.addObject("inProgressOrders", orderRepository.findByStatus(Order.Status.IN_PROGRESS));
        model.addObject("completedOrders", orderRepository.findByStatus(Order.Status.COMPLETED));
        return model;
    }

    @RequestMapping(value = "/kanban", method = RequestMethod.POST)
    public String moveOrder(@RequestParam Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            order.setStatus(Order.Status.COMPLETED);
        } else if (order.getStatus() == Order.Status.CREATED) {
            order.setStatus(Order.Status.IN_PROGRESS);
        }
        orderRepository.save(order);
        return "redirect:/kanban";
    }
}
