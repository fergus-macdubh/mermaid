package com.vfasad.controller;

import com.vfasad.entity.Order;
import com.vfasad.entity.OrderConsume;
import com.vfasad.entity.Product;
import com.vfasad.repo.OrderRepository;
import com.vfasad.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;

import static com.vfasad.entity.User.*;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER, ROLE_SALES})
    public ModelAndView products() {
        ModelAndView model = new ModelAndView("order/order-dashboard");
        model.addObject("orders", orderRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView addOrderForm() {
        ModelAndView model = new ModelAndView("order/order-form");
        model.addObject("orders", orderRepository.findAll());
        model.addObject("products", productRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String addProduct(
            @RequestParam int area,
            @RequestParam String client,
            @RequestParam double price,
            @RequestParam long[] productIds,
            @RequestParam int[] quantities) {
        Set<OrderConsume> consumes = new HashSet<>();

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
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView editProductForm(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("order/order-form");
        model.addObject("order", orderRepository.findOne(id));
        model.addObject("products", productRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam int area,
            @RequestParam String client,
            @RequestParam double price,
            @RequestParam long[] productIds,
            @RequestParam int[] quantities) {
        Set<OrderConsume> consumes = new HashSet<>();

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
