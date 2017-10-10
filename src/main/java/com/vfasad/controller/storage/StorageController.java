package com.vfasad.controller.storage;

import com.vfasad.dto.Product;
import com.vfasad.dto.ProductAction;
import com.vfasad.repo.ProductActionRepository;
import com.vfasad.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StorageController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductActionRepository productActionRepository;

    @RequestMapping(value = "/storage", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView model = new ModelAndView("storage/storage-dashboard");
        model.addObject("products", productRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/storage/product/{id}/action", method = RequestMethod.GET)
    public ModelAndView productActions(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("storage/product-actions");
        model.addObject("actions", productActionRepository.findByproductId(id));
        return model;
    }

    @RequestMapping(value = "/storage/product/purchase", method = RequestMethod.GET)
    public ModelAndView purchaseproductForm() {
        ModelAndView model = new ModelAndView("storage/purchase-product-form");
        model.addObject("products", productRepository.findAll());
        return model;
    }

    @RequestMapping(value = "/storage/product/purchase", method = RequestMethod.POST)
    public String purchaseproduct(
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
        productRepository.save(product);
        return "redirect:/storage";
    }
}
