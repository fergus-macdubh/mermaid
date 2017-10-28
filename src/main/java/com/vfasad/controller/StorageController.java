package com.vfasad.controller;

import com.vfasad.service.ProductService;
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

import static com.vfasad.entity.User.*;

@Controller
@Validated
public class StorageController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/storage", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER})
    public ModelAndView dashboard() {
        ModelAndView model = new ModelAndView("storage/storage-dashboard");
        model.addObject("storagePrice", productService.getStoragePrice());
        model.addObject("products", productService.findAllProductsInStorage());
        return model;
    }

    @RequestMapping(value = "/storage/product/{id}/action", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER})
    public ModelAndView productActions(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("storage/product-actions");
        model.addObject("product", productService.findProduct(id));
        model.addObject("actions", productService.findAllProductActions(id));
        return model;
    }

    @RequestMapping(value = "/storage/product/purchase", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView purchaseProductForm() {
        ModelAndView model = new ModelAndView("storage/purchase-product-form");
        model.addObject("products", productService.findAllProducts());
        return model;
    }

    @RequestMapping(value = "/storage/product/purchase", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String purchaseProduct(
            @RequestParam Long productId,
            @RequestParam @Min(value = 0, message = "Quantity cannot be zero or negative.")
                    double quantity,
            @RequestParam @Min(value = 0, message = "Price cannot be zero or negative.")
                    double price) {
        productService.purchase(productId, quantity, price);
        return "redirect:/storage";
    }

    @RequestMapping(value = "/storage/product/{id}/inventorying", method = RequestMethod.GET)
    @Secured(ROLE_ADMIN)
    public ModelAndView productInventoryingForm(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("storage/inventorying-product-form");
        model.addObject("product", productService.getProduct(id));
        return model;
    }

    @RequestMapping(value = "/storage/product/{id}/inventorying", method = RequestMethod.POST)
    @Secured(ROLE_ADMIN)
    public String productInventorying(
            @PathVariable Long id,
            @RequestParam double quantity,
            @RequestParam double price) {
        productService.performInventorying(id, quantity, price);
        return "redirect:/storage";
    }
}
