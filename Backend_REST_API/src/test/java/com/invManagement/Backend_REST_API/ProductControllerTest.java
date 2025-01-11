package com.invManagement.Backend_REST_API;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.invManagement.Backend_REST_API.model.Product;
import com.invManagement.Backend_REST_API.model.ProductRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository repository;

    private final Product validProduct = new Product("1234", "Test", 599);

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void testAddNewProduct_validProduct() throws Exception {

        mockMvc.perform(
                    MockMvcRequestBuilders.post("/product/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validProduct))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.barcode").value("1234"));
    }

    @Test
    public void testAddNewProduct_validProductAlreadyExists() throws Exception {

        repository.save(validProduct);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validProduct))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Product with the same barcode already exists."));
    }

    @Test
    public void testAddNewProduct_missingBuyPrice() throws Exception {
        Product product = new Product("1234", "Test", null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Buy Price is required."));
    }

    @Test
    public void testAddNewProduct_missingBarcode() throws Exception {
        Product product = new Product(null, "Test", 599);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Barcode is required."));
    }

    @Test
    public void testAddNewProduct_missingName() throws Exception {
        var product = new Product("1234", null, 599);
        product.setBarcode("1234");
        product.setBuyPrice(599);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Name is required."));
    }

    @Test
    public void testAddNewProduct_missingMultipleFields() throws Exception {
        var product = new Product(); // Missing all fields except the ID.

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Barcode is required.")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Name is required.")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Buy Price is required.")));
    }

    @Test
    public void testUpdateProduct_validUpdate() throws Exception {
        repository.save(validProduct);

        //Request Body
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("barcode", "1234");
        updateRequest.put("name", "Updated Name");
        updateRequest.put("buyPrice", 699);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.barcode").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyPrice").value(699));

    }

    @Test
    public void testUpdateProduct_missingBarcode() throws Exception {
        repository.save(validProduct);

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("name", "Updated Name");
        updateRequest.put("buyPrice", 699);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Barcode is required to identify the product."));
    }

    @Test
    public void testUpdateProduct_productNotFound() throws Exception {

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("barcode", "nonexistentBarcode");
        updateRequest.put("name", "Updated Name");
        updateRequest.put("buyPrice", 699);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product with the specified barcode does not exist."));
    }

    @Test
    public void testUpdateProduct_nonIntegerBuyPrice() throws Exception {
        repository.save(validProduct);

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("barcode", "1234");
        updateRequest.put("buyPrice", 4.32);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Buy Price must be an integer."));
    }

    @Test
    public void testUpdateProduct_negativeBuyPrice() throws Exception {
        repository.save(validProduct);

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("barcode", "1234");
        updateRequest.put("buyPrice", -400);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Buy Price must be a positive integer."));
    }




}
