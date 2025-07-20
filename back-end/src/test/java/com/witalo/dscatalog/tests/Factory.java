package com.witalo.dscatalog.tests;

import com.witalo.dscatalog.dto.ProductDTO;
import com.witalo.dscatalog.entites.Category;
import com.witalo.dscatalog.entites.Product;
import org.checkerframework.checker.units.qual.C;

import java.time.Instant;

public class Factory {
    public static Product createProduct() {
        Product product = new Product(1L, "Phone","Good Phone", 800.0, "", Instant.now());
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, createProduct().getCategories());
    }

    public static Category createCategory() {
        return new Category(2L, "Electronics");
    }
}
