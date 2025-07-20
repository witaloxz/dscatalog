package com.witalo.dscatalog.repositories;

import com.witalo.dscatalog.entites.Product;
import com.witalo.dscatalog.services.exceptions.ResourceNotFoundException;
import com.witalo.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.jsf.FacesContextUtils;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long existingNotId;
    private Long countTotalProducts;

    @BeforeEach
    public void setUp(){
        existingId = 1L;
        existingNotId = 999990099090900L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsnull(){
        Product product = Factory.createProduct();
        product.setId(null);
        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1,product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertFalse(result.isPresent());

    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExists(){
        productRepository.findById(existingId);
        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertNotNull(result.get());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Optional<Product> result = productRepository.findById(existingNotId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataExceptionWhenIdDoesNotExist(){


        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
        productRepository.deleteById(existingNotId);

        });
    }

}
