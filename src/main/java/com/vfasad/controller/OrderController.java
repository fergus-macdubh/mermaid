package com.vfasad.controller;

import com.vfasad.dto.Order;
import com.vfasad.dto.OrderConsume;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

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
        model.addObject("products", productRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    public String addProduct(
            @RequestParam int area,
            @RequestParam String client,
            @RequestParam double price,
            @RequestParam long[] productIds,
            @RequestParam int[] quantities) {
        List<OrderConsume> consumes = new ArrayList<>();

        for (int i = 0; i < productIds.length; i++) {
            Product product = productRepository.findOne(productIds[i]);
            consumes.add(new OrderConsume(product, quantities[i]));
        }

        orderRepository.save(new Order(
                null, // todo: get this from credentials
                area,
                client,
                price,
                consumes));
        return "redirect:/order";
    }

    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.GET)
    public ModelAndView editProductForm(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("order/order-form");
        model.addObject("order", orderRepository.findOne(id));
        model.addObject("products", productRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.POST)
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam int area,
            @RequestParam String client,
            @RequestParam double price,
            @RequestParam long[] productIds,
            @RequestParam int[] quantities) {
        List<OrderConsume> consumes = new ArrayList<>();

        for (int i = 0; i < productIds.length; i++) {
            Product product = productRepository.findOne(productIds[i]);
            consumes.add(new OrderConsume(product, quantities[i]));
        }

        Order order = orderRepository.findOne(id);
        order.setArea(area);
        order.setClient(client);
        order.setPrice(price);
        order.setConsumes(consumes);
        orderRepository.save(order);
        return "redirect:/order";
    }
}
