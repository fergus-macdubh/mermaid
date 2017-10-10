package com.vfasad.controller.storage;

import com.vfasad.dto.Order;
import com.vfasad.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
        order.setStatus(Order.Status.IN_PROGRESS);
        orderRepository.save(order);
        return "redirect:/kanban";
    }

//    @RequestMapping(value = "/order/add", method = RequestMethod.GET)
//    public ModelAndView addProductForm() {
//        ModelAndView model = new ModelAndView("order/order-form");
//        model.addObject("orders", orderRepository.findAll());
//        return model;
//    }

//    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
//    public String addProduct(
//            @RequestParam int area,
//            @RequestParam String client,
//            @RequestParam double price) {
//        orderRepository.save(new Order(
//                null, // todo: get this from credentials
//                area,
//                client,
//                price));
//        return "redirect:/order";
//    }
//
//    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.GET)
//    public ModelAndView editProductForm(@PathVariable Long id) {
//        ModelAndView model = new ModelAndView("order/order-form");
//        model.addObject("order", orderRepository.findOne(id));
//        return model;
//    }
//
//    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.POST)
//    public String updateProduct(
//            @PathVariable Long id,
//            @RequestParam int area,
//            @RequestParam String client,
//            @RequestParam double price) {
//        Order order = orderRepository.findOne(id);
//        order.setArea(area);
//        order.setClient(client);
//        order.setPrice(price);
//        orderRepository.save(order);
//        return "redirect:/order";
//    }
}
