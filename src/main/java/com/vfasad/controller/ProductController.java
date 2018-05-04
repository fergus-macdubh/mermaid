package com.vfasad.controller;

import com.vfasad.entity.Product;
import com.vfasad.service.ProductService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static com.vfasad.entity.User.ROLE_ADMIN;
import static com.vfasad.entity.User.ROLE_OPERATOR;

@Controller
@Validated
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView products() {
        ModelAndView model = new ModelAndView("product/product-dashboard");
        model.addObject("products", productService.getProducts());
        return model;
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView addProductForm() {
        ModelAndView model = new ModelAndView("product/add-product-form");
        model.addObject("products", productService.getProducts());
        return model;
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String addProduct(
            @RequestParam @NotEmpty(message = "Name may not be empty") String name,
            @RequestParam @NotEmpty(message = "Producer may not be empty") String producer,
            @RequestParam(required = false) String supplier,
            @RequestParam Product.Unit unit) {
        productService.add(new Product(
                name,
                unit,
                producer,
                supplier));
        return "redirect:/product";
    }

    @RequestMapping(value = "/product/{id}/edit", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView editProductForm(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("product/add-product-form");
        model.addObject("product", productService.getProduct(id));
        return model;
    }

    @RequestMapping(value = "/product/{id}/edit", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam @NotEmpty(message = "Name may not be empty") String name,
            @RequestParam @NotEmpty(message = "Producer may not be empty") String producer,
            @RequestParam String supplier,
            @RequestParam Product.Unit unit) {
        productService.updateProduct(id, name, producer, supplier, unit);
        return "redirect:/product";
    }

    @PostMapping("/product/{id}/delete")
    @Secured(ROLE_ADMIN)
    public String deleteProduct(
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/product";
    }
}
