package com.vfasad.controller;

import com.vfasad.entity.Product;
import com.vfasad.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static com.vfasad.entity.User.ROLE_ADMIN;
import static com.vfasad.entity.User.ROLE_OPERATOR;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView products() {
        ModelAndView model = new ModelAndView("product/product-dashboard");
        model.addObject("products", productRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView addProductForm() {
        ModelAndView model = new ModelAndView("product/add-product-form");
        model.addObject("products", productRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/product/add", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String addProduct(
            @RequestParam String name,
            @RequestParam String producer,
            @RequestParam String supplier,
            @RequestParam Product.Unit unit) {
        productRepository.save(new Product(
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
        model.addObject("product", productRepository.findOne(id));
        return model;
    }

    @RequestMapping(value = "/product/{id}/edit", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String producer,
            @RequestParam String supplier,
            @RequestParam Product.Unit unit) {
        Product product = productRepository.findOne(id);
        product.setName(name);
        product.setProducer(producer);
        product.setSupplier(supplier);
        product.setUnit(unit);
        productRepository.save(product);
        return "redirect:/product";
    }
}
