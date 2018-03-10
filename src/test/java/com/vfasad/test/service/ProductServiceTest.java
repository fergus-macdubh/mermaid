package com.vfasad.test.service;

import com.vfasad.entity.Product;
import com.vfasad.repo.ProductActionRepository;
import com.vfasad.repo.ProductRepository;
import com.vfasad.service.ProductService;
import com.vfasad.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @Mock
    ProductActionRepository productActionRepository;

    @Mock
    UserService userService;

    @InjectMocks
    ProductService productService;

    @Test
    public void testExample() {
        when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Product("name", Product.Unit.KILOGRAM, "producer", "supplier")));

        Product p = productService.getProduct(1L);
        assertNotNull("Product must not be null", p);
        assertEquals("Incorrect name", "name", p.getName());

        verify(productRepository, times(1)).findById(1L);
    }
}
