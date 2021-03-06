package com.vfasad.service;

import com.vfasad.entity.Order;
import com.vfasad.entity.Product;
import com.vfasad.entity.ProductAction;
import com.vfasad.exception.NotEnoughResourceException;
import com.vfasad.exception.NotFoundException;
import com.vfasad.repo.ProductActionRepository;
import com.vfasad.repo.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Product> findAllProducts() {
        return productRepository.findByDeletedIsFalseOrderByName();
    }

    public List<Product> findAllProductsInStorage() {
        return productRepository.findByQuantityGreaterThanAndDeletedIsFalseOrderByName(0);
    }

    public List<ProductAction> findAllProductActions(Long id) {
        return productActionRepository.getByProductIdOrderByCreatedDesc(id);
    }

    public void purchase(Long productId, double quantity, double price) {
        Product product = getProduct(productId);

        productActionRepository.save(ProductAction.createPurchaseAction(
                quantity,
                price * quantity,
                product,
                userService.getCurrentUser()
        ));

        product.setQuantity(product.getQuantity() + quantity);
        product.setPrice(price);
        productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with provided id is not found."));
    }

    public void performInventorying(Long id, double quantity, double price) {
        Product product = getProduct(id);
        productActionRepository.save(ProductAction.createInventoryingAction(
                quantity - product.getQuantity(),
                product,
                userService.getCurrentUser()
        ));
        product.setQuantity(quantity);
        product.setPrice(price);
        productRepository.save(product);
    }

    public List<Product> getProducts() {
        return productRepository.findByDeletedIsFalseOrderById();
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
        if (product.getQuantity() + remain < 0) {
            throw new NotEnoughResourceException(
                    String.format("Not enough resource [%s] in storage. Order [%s].",
                            product.getName(),
                            order.getId()));
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

    public double getStoragePrice() {
        return productRepository.getStoragePrice();
    }

    public Product findProduct(Long id) {
        return productRepository.findOne(id);
    }

    public void deleteProduct(Long id) {
        Product product = getProduct(id);
        product.setDeleted(true);
        productRepository.save(product);
    }
}
