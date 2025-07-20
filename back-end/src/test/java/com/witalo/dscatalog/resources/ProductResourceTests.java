package com.witalo.dscatalog.resources;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.witalo.dscatalog.dto.ProductDTO;
import com.witalo.dscatalog.services.ProductService;
import com.witalo.dscatalog.services.exceptions.ResourceNotFoundException;
import com.witalo.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    private static final Logger log = LoggerFactory.getLogger(ProductResourceTests.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;


    private Long existingId;
    private Long existingNotid;
    private Long dependtId;

    @BeforeEach
    public void setUp() throws Exception{

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        existingId = 1L;
        existingNotid = 2L;
        dependtId = 3L;

        Mockito.when(productService.findAllPaged(Mockito.any(Pageable.class))).thenReturn(page);

        Mockito.when(productService.findById(existingId)).thenReturn(productDTO);
        Mockito.when(productService.findById(existingNotid)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(productService.update(Mockito.eq(existingId), Mockito.any(ProductDTO.class))).thenReturn(productDTO);
        Mockito.when(productService.update(Mockito.eq(existingNotid), Mockito.any(ProductDTO.class))).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(productService).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(productService).delete(existingNotid);

        Mockito.when(productService.insert(Mockito.any(ProductDTO.class))).thenReturn(productDTO);

    }

    @Test
    public void insertShouldReturnCreatedProductDTO() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(jsonBody))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }
    @Test
    public void  deleteShouldReturnNoContentWhenIdExists() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", existingNotid))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonBody))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());


    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingNotid)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }




    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}",existingId)).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }


    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}",existingNotid)).
                andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products")).
                andExpect(MockMvcResultMatchers.status().isOk());

    }
}
