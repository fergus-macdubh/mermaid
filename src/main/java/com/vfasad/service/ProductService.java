package com.vfasad.service;

import com.vfasad.entity.Order;
import com.vfasad.entity.Product;
import com.vfasad.entity.ProductAction;
import com.vfasad.exception.NotFoundException;
import com.vfasad.repo.ProductActionRepository;
import com.vfasad.repo.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductActionRepository productActionRepository;

    @Autowired
    private UserService userService;

    public List<Product> findAllProductsInStorage() {
        return productRepository.findByQuantityGreaterThan(0);
    }

    public List<ProductAction> findAllProductActions(Long id) {
        return productActionRepository.getByProductIdOrderByCreatedDesc(id);
    }

    public List<Product> findAll() {
        return productRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "id")));
    }

    public void purchase(Long productId, double quantity, double price) {
        Product product = getProduct(productId);

        productActionRepository.save(ProductAction.createPurchaseAction(
                quantity,
                price,
                product,
                userService.getCurrentUser()
        ));

        product.setQuantity(product.getQuantity() + quantity);
        product.setPrice(price / quantity);
        productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with provided id is not found."));
    }

    public void performInventorying(Long id, double quantity) {
        Product product = getProduct(id);
        productActionRepository.save(ProductAction.createInventoryingAction(
                quantity - product.getQuantity(),
                product,
                userService.getCurrentUser()
        ));
        product.setQuantity(quantity);
        productRepository.save(product);
    }

    public List<Product> getProducts() {
        return productRepository.findAllByOrderById();
    }

    public void add(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(Long id, String name, String producer, String supplier, Product.Unit unit) {
        Product product = getProduct(id);
        product.setName(name);
        product.setProducer(producer);
        product.setSupplier(supplier);
        product.setUnit(unit);
        productRepository.save(product);
    }

    public void spend(Product product, double quantity, Order order) {
        if (quantity <= 0) {
            return;
        }

        productActionRepository.save(ProductAction.createSpendAction(
                quantity,
                product,
                userService.getCurrentUser(),
                order
        ));
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    public void returnProduct(Product product, double remain, Order order) {
        if (remain <= 0) {
            return;
        }

        productActionRepository.save(ProductAction.createReturnAction(
                remain,
                product,
                userService.getCurrentUser(),
                order
        ));
        product.setQuantity(product.getQuantity() + remain);
        productRepository.save(product);
    }
}
