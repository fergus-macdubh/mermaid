package com.vfasad.controller.storage;

import com.vfasad.dto.Order;
import com.vfasad.dto.Product;
import com.vfasad.repo.OrderRepository;
import com.vfasad.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public ModelAndView products() {
        ModelAndView model = new ModelAndView("order/order-dashboard");
        model.addObject("orders", orderRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.GET)
    public ModelAndView addProductForm() {
        ModelAndView model = new ModelAndView("order/order-form");
        model.addObject("orders", orderRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    public String addProduct(
            @RequestParam int area,
            @RequestParam String client,
            @RequestParam double price) {
        orderRepository.save(new Order(
                null, // todo: get this from credentials
                area,
                client,
                price));
        return "redirect:/order";
    }

    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.GET)
    public ModelAndView editProductForm(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("order/order-form");
        model.addObject("order", orderRepository.findOne(id));
        return model;
    }

    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.POST)
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam int area,
            @RequestParam String client,
            @RequestParam double price) {
        Order order = orderRepository.findOne(id);
        order.setArea(area);
        order.setClient(client);
        order.setPrice(price);
        orderRepository.save(order);
        return "redirect:/order";
    }
}
