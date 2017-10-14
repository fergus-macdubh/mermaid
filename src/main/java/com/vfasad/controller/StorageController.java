package com.vfasad.controller;

import com.vfasad.entity.Product;
import com.vfasad.entity.ProductAction;
import com.vfasad.repo.ProductActionRepository;
import com.vfasad.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static com.vfasad.entity.User.*;

@Controller
public class StorageController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductActionRepository productActionRepository;

    @RequestMapping(value = "/storage", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER})
    public ModelAndView dashboard() {
        Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();

        System.out.println(userDetails);

        ModelAndView model = new ModelAndView("storage/storage-dashboard");
        model.addObject("products", productRepository.findByQuantityGreaterThan(0));
        return model;
    }

    @RequestMapping(value = "/storage/product/{id}/action", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR, ROLE_PAINTER})
    public ModelAndView productActions(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("storage/product-actions");
        model.addObject("actions", productActionRepository.findByproductId(id));
        return model;
    }

    @RequestMapping(value = "/storage/product/purchase", method = RequestMethod.GET)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public ModelAndView purchaseProductForm() {
        ModelAndView model = new ModelAndView("storage/purchase-product-form");
        model.addObject("products", productRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/storage/product/purchase", method = RequestMethod.POST)
    @Secured({ROLE_ADMIN, ROLE_OPERATOR})
    public String purchaseProduct(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam double price) {
        Product product = productRepository.findOne(productId);

        productActionRepository.save(new ProductAction(
                quantity,
                price,
                ProductAction.Type.PURCHASE,
                product,
                null // todo: fill this from credentials
        ));

        product.setQuantity(product.getQuantity() + quantity);
        product.setPrice(price / quantity);
        productRepository.save(product);
        return "redirect:/storage";
    }
}
