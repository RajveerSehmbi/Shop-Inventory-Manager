package com.invManagement.Backend_REST_API;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invManagement.Backend_REST_API.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddNewProduct_validProduct() throws Exception {
        var product = new Product();
        product.setBarcode("1234");
        product.setName("Test");
        product.setBuyPrice(599);

        mockMvc.perform(
                    MockMvcRequestBuilders.post("/product/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAddNewProduct_missingBuyPrice() throws Exception {
        var product = new Product();
        product.setBarcode("1234");
        product.setName("Test");

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
        var product = new Product();
        product.setName("Test");
        product.setBuyPrice(599);

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
        var product = new Product();
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


}
