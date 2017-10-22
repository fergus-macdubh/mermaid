package com.vfasad.controller;

import com.vfasad.entity.OrderConsume;
import com.vfasad.entity.Product;
import com.vfasad.entity.User;
import com.vfasad.service.OrderService;
import com.vfasad.service.ProductService;
import com.vfasad.service.UserService;
import com.vfasad.validation.constraints.ElementMin;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.vfasad.entity.User.*;

@Controller
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_SALES})
    public ModelAndView orders() {
        ModelAndView model = new ModelAndView("order/order-dashboard");
        model.addObject("orders", orderService.findAll());
        return model;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView addOrderForm() {
        ModelAndView model = new ModelAndView("order/order-form");
        model.addObject("orders", orderService.findAll());
        model.addObject("products", productService.findAll());
        model.addObject("managers", userService.getManagers());
        return model;
    }

    @RequestMapping(value = "/order/add", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String addOrder(
            @RequestParam @Min(value = 1, message = "Area cannot be zero or negative.")
                    double area,
            @RequestParam(required = false)
                    String client,
            @RequestParam @Min(value = 1, message = "Price cannot be zero or negative.")
                    double price,
            @RequestParam @NotEmpty
                    long[] productIds,
            @RequestParam @NotEmpty @ElementMin(value = 1, message = "Quantities cannot be zero or negative.")
                    List<Double> quantities,
            @RequestParam long managerId) {
        Set<OrderConsume> consumes = new HashSet<>();

        for (int i = 0; i < productIds.length; i++) {
            Product product = productService.getProduct(productIds[i]);
            consumes.add(new OrderConsume(product, quantities.get(i)));
        }

        orderService.addOrder(area, client, price, consumes, userService.getUser(managerId));
        return "redirect:/order";
    }

    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView editProductForm(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("order/order-form");
        model.addObject("order", orderService.getOrder(id));
        model.addObject("products", productService.findAll());
        model.addObject("managers", userService.getManagers());
        return model;
    }

    @RequestMapping(value = "/order/{id}/edit", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam double area,
            @RequestParam String client,
            @RequestParam double price,
            @RequestParam long[] productIds,
            @RequestParam @NotEmpty
            @ElementMin(value = 1, message = "Quantities cannot be zero or negative.")
                    List<Integer> quantities,
            @RequestParam Long managerId) {
        Set<OrderConsume> consumes = new HashSet<>();

        for (int i = 0; i < productIds.length; i++) {
            Product product = productService.getProduct(productIds[i]);
            consumes.add(new OrderConsume(product, quantities.get(i)));
        }
        User manager = userService.getUser(managerId);
        orderService.updateOrder(id, area, client, price, consumes, manager);
        return "redirect:/order";
    }
}
