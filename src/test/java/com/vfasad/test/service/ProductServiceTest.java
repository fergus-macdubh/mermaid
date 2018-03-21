package com.vfasad.test.service;

import com.vfasad.entity.Order;
import com.vfasad.entity.Product;
import com.vfasad.entity.ProductAction;
import com.vfasad.entity.User;
import com.vfasad.exception.NotEnoughResourceException;
import com.vfasad.exception.NotFoundException;
import com.vfasad.repo.ProductActionRepository;
import com.vfasad.repo.ProductRepository;
import com.vfasad.service.ProductService;
import com.vfasad.service.UserService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
    private static final Long PRODUCT_ID = 1L;
    private static final String NAME = "name";
    private static final Product.Unit UNIT = Product.Unit.KILOGRAM;
    private static final String PRODUCER = "producer";
    private static final String SUPPLIER = "supplier";
    private static final double QUANTITY = 2.3;
    private static final double PRICE = 9483.84;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductActionRepository productActionRepository;

    @Mock
    private UserService userService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private ProductService productService;

    @Test
    public void testFindAllProducts() {
        List<Product> productList = generateProductList();

        when(productRepository.findAllByOrderByName()).thenReturn(productList);

        List<Product> resultProductList = productService.findAllProducts();
        assertEquals("Product lists should be equal", productList, resultProductList);
    }

    @Test
    public void testFindAllProductsInStorage() {
        List<Product> productList = generateProductList();

        when(productRepository.findByQuantityGreaterThanOrderByName(0)).thenReturn(productList);

        List<Product> resultProductList = productService.findAllProductsInStorage();
        assertEquals("Product lists should be equal", productList, resultProductList);
    }

    @Test
    public void testFindAllProductActions() {
        List<ProductAction> productActionList = generateProductActionList();

        when(productActionRepository.getByProductIdOrderByCreatedDesc(PRODUCT_ID)).thenReturn(productActionList);

        List<ProductAction> resultProductActionList = productService.findAllProductActions(PRODUCT_ID);
        assertEquals("Product action lists should be equal", productActionList, resultProductActionList);
    }

    @Test
    public void testPurchase() {
        double additionalQuantity = 4.50;
        double newPrice    = 3882.89;
        Product product = generateProduct();
        User user = generateUser();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(userService.getCurrentUser()).thenReturn(user);

        productService.purchase(PRODUCT_ID, additionalQuantity, newPrice);
        assertEquals("Invalid product quantity", QUANTITY + additionalQuantity, product.getQuantity(), 0.00);
        assertEquals("Invalid product price", newPrice, product.getPrice(), 0.00);
        verify(productActionRepository, times(1)).save(any(ProductAction.class));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetProduct() {
        Product product = generateProduct();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product resultProduct = productService.getProduct(PRODUCT_ID);
        assertNotNull("Product must not be null", resultProduct);
        assertEquals("Incorrect id", PRODUCT_ID, resultProduct.getId());
        assertEquals("Incorrect quantity", QUANTITY, resultProduct.getQuantity(), 0.00);
        assertEquals("Incorrect price", PRICE, resultProduct.getPrice(), 0.00);
        assertEquals("Incorrect name", NAME, resultProduct.getName());
        assertEquals("Incorrect Unit", UNIT, resultProduct.getUnit());
        assertEquals("Incorrect producer", PRODUCER, resultProduct.getProducer());
        assertEquals("Incorrect supplier", SUPPLIER, resultProduct.getSupplier());
    }

    @Test
    public void testGetProductNotFoundException() {
        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage("Product with provided id is not found.");

        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        productService.getProduct(PRODUCT_ID);
    }

    @Test
    public void testPerformInventorying() {
        double newQuantity = 4.50;
        double newPrice    = 3.6;
        Product product = generateProduct();
        User user = generateUser();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(userService.getCurrentUser()).thenReturn(user);

        productService.performInventorying(PRODUCT_ID, newQuantity, newPrice);
        assertEquals("Invalid product quantity", newQuantity, product.getQuantity(), 0.00);
        assertEquals("Invalid product price", newPrice, product.getPrice(), 0.00);
        verify(productActionRepository, times(1)).save(any(ProductAction.class));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetProducts() {
        List<Product> productList = generateProductList();
        when(productRepository.findAllByOrderById()).thenReturn(productList);

        List<Product> resultProductList = productService.getProducts();
        assertEquals("Product lists should be equal", productList, resultProductList);
    }

    @Test
    public void testAdd() {
        Product product = generateProduct();

        productService.add(product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testUpdateProduct() {
        Product product = generateProduct();
        String newName = "newName";
        String newProducer = "newProducer";
        String newSupplier = "newSupplier";
        Product.Unit newUnit = Product.Unit.LITER;

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        productService.updateProduct(PRODUCT_ID, newName, newProducer, newSupplier, newUnit);
        assertEquals("Incorrect name", newName, product.getName());
        assertEquals("Incorrect Unit", newUnit, product.getUnit());
        assertEquals("Incorrect producer", newProducer, product.getProducer());
        assertEquals("Incorrect supplier", newSupplier, product.getSupplier());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testSpendPositive() {
        double spendQuantity = 5.6;
        Product product = generateProduct();
        User user = generateUser();
        Order order = generateOrder(user);

        productService.spend(product, spendQuantity, order);

        assertEquals("Product quantity should be changed",
                QUANTITY - spendQuantity,
                product.getQuantity(), 0.00);
        verify(productActionRepository, times(1)).save(any(ProductAction.class));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testSpendNegative() {
        double spendQuantity = -5.6;
        testSpendNegative(spendQuantity);

        spendQuantity = 0.00;
        testSpendNegative(spendQuantity);
    }

    private void testSpendNegative(double spendQuantity) {
        Product product = generateProduct();
        User user = generateUser();
        Order order = generateOrder(user);

        productService.spend(product, spendQuantity, order);

        assertEquals("Quantity shouldn't be changed", QUANTITY, product.getQuantity(), 0.00);
        verify(productActionRepository, never()).save(any(ProductAction.class));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testReturnProduct() {
        double remain = QUANTITY;
        Product product = generateProduct();
        User user = generateUser();
        Order order = generateOrder(user);

        productService.returnProduct(product, remain, order);

        assertEquals("Incorrect product quantity", QUANTITY + remain, product.getQuantity(), 0.00);
        verify(productActionRepository, times(1)).save(any(ProductAction.class));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testReturnProductException() {
        double remain = QUANTITY - 4 * QUANTITY;
        Product product = generateProduct();
        User user = generateUser();
        Order order = generateOrder(user);

        expectedException.expect(NotEnoughResourceException.class);
        expectedException.expectMessage(
                String.format("Not enough resource [%s] in storage. Order [%s].",
                product.getName(),
                order.getId()));
        productService.returnProduct(product, remain, order);
    }

    @Test
    public void testGetStoragePrice() {
        final double storagePrice = 10.28;
        when(productRepository.getStoragePrice()).thenReturn(storagePrice);

        double resultPrice = productService.getStoragePrice();
        assertEquals("Incorrect price", resultPrice, storagePrice, 0.00);
    }

    @Test
    public void testFindProduct() {
        Product product = generateProduct();

        when(productRepository.findOne(anyLong())).thenReturn(product);

        Product resultProduct = productService.findProduct(PRODUCT_ID);
        assertEquals("Incorrect id", PRODUCT_ID, resultProduct.getId());
        assertEquals("Incorrect name", NAME, resultProduct.getName());
        assertEquals("Incorrect Unit", UNIT, resultProduct.getUnit());
        assertEquals("Incorrect producer", PRODUCER, resultProduct.getProducer());
        assertEquals("Incorrect supplier", SUPPLIER, resultProduct.getSupplier());
        assertEquals("Incorrect quantity", QUANTITY, resultProduct.getQuantity(), 0.00);
        assertEquals("Incorrect price", PRICE, resultProduct.getPrice(), 0.00);
    }

    private List<Product> generateProductList() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("name1", Product.Unit.KILOGRAM, "producer1","supplier1"));
        productList.add(new Product("name2", Product.Unit.ITEM, "producer2","supplier2"));
        productList.add(new Product("name3", Product.Unit.LITER, "producer3","supplier3"));

        return productList;
    }

    private List<ProductAction> generateProductActionList() {
        List<ProductAction> productActionList = new ArrayList<>();
        Product product = new Product("name1", Product.Unit.KILOGRAM,"producer1", "supplier1");
        User user = new User("email1","name1","givenName1","familyName1",null,"female","en");

        productActionList.add(ProductAction.createInventoryingAction(1, product, user));

        product = new Product("name2", Product.Unit.LITER,"producer2", "supplier2");
        user = new User("email2","name2","givenName2","familyName2",null,"male","en");
        Order order = generateOrder(user);

        productActionList.add(ProductAction.createReturnAction(3, product, user, order));

        return productActionList;
    }

    private User generateUser() {
        return new User("email1","name1","givenName1","familyName1",null,"female","en");
    }

    private Product generateProduct() {
        Product product = new Product(NAME, UNIT, PRODUCER, SUPPLIER);
        product.setId(PRODUCT_ID);
        product.setQuantity(QUANTITY);
        product.setPrice(PRICE);

        return product;
    }

    private Order generateOrder(User user) {
        return new Order(user, 3.4, 2, 5, 6, "abc", 6.9, new HashSet<>(), LocalDate.now());
    }
}
