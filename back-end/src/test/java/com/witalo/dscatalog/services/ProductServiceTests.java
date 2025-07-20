package com.witalo.dscatalog.services;

import com.witalo.dscatalog.dto.ProductDTO;
import com.witalo.dscatalog.entites.Category;
import com.witalo.dscatalog.entites.Product;
import com.witalo.dscatalog.repositories.CategoryRepository;
import com.witalo.dscatalog.repositories.ProductRepository;
import com.witalo.dscatalog.services.exceptions.DataBaseException;
import com.witalo.dscatalog.services.exceptions.ResourceNotFoundException;
import com.witalo.dscatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long existingNotId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;


    @BeforeEach
    public void setUp() throws Exception{
        existingId = 1L;
        existingNotId = 1000L;
        dependentId = 3L;
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));

        Mockito.when(productRepository.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(existingNotId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.getOne(existingId)).thenReturn(product);
        Mockito.when(productRepository.getOne(existingNotId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(existingNotId)).thenThrow(EntityNotFoundException.class);

        Mockito.doNothing().when(productRepository).deleteById(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(existingNotId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(existingNotId,productDTO);
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExist(){
        ProductDTO result = productService.update(existingId,productDTO);
        Assertions.assertNotNull(result);

    }

    @Test
    public void findByidShouldThrowResourceNotFoundExceptionDoesNotExistId(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           productService.findById(existingNotId);
        });
    }

    @Test
    public void findByidShouldWhenProductDtoReturnIdExist(){
        ProductDTO result = productService.findById(existingId);
        Assertions.assertNotNull(result);
    }
    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = productService.findAllPaged(pageable);

        Assertions.assertNotNull(result);


        Mockito.verify(productRepository,Mockito.times(1)).findAll(pageable);
    }
    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependId(){
        Assertions.assertThrows(DataBaseException.class, () -> {
            productService.delete(dependentId);
        });

        Mockito.verify(productRepository,Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(existingNotId);
        });

        Mockito.verify(productRepository,Mockito.times(1)).deleteById(existingNotId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        Mockito.verify(productRepository,Mockito.times(1)).deleteById(existingId);
    }
}
