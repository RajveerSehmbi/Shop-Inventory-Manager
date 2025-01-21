package com.invManagement.Backend_REST_API;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.invManagement.Backend_REST_API.models.Product.Product;
import com.invManagement.Backend_REST_API.models.Product.ProductRepository;
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


    //---------------------------------POST ENDPOINT---------------------------------//
    @Test
    public void testPostProduct_validProduct() throws Exception {

        mockMvc.perform(
                    MockMvcRequestBuilders.post("/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validProduct))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.barcode").value("1234"));
    }

    @Test
    public void testPostProduct_validProductAlreadyExists() throws Exception {

        repository.save(validProduct);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validProduct))
                )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string("Product with the same barcode already exists."));
    }

    @Test
    public void testPostProduct_missingBuyPrice() throws Exception {
        Product product = new Product("1234", "Test", null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Buy Price is required."));
    }

    @Test
    public void testPostProduct_missingBarcode() throws Exception {
        Product product = new Product(null, "Test", 599);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Barcode is required."));
    }

    @Test
    public void testPostProduct_missingName() throws Exception {
        var product = new Product("1234", null, 599);
        product.setBarcode("1234");
        product.setBuyPrice(599);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Name is required."));
    }

    @Test
    public void testPostProduct_missingMultipleFields() throws Exception {
        var product = new Product(); // Missing all fields except the ID.

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Barcode is required.")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Name is required.")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Buy Price is required.")));
    }


    //---------------------------------PUT ENDPOINT---------------------------------//
    @Test
    public void testPutProduct_validUpdate() throws Exception {
        repository.save(validProduct);

        //Request Body
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("barcode", "1234");
        updateRequest.put("name", "Updated Name");
        updateRequest.put("buyPrice", 699);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.barcode").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyPrice").value(699));

    }

    @Test
    public void testPatchProduct_missingBarcode() throws Exception {
        repository.save(validProduct);

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("name", "Updated Name");
        updateRequest.put("buyPrice", 699);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Barcode is required to identify the product."));
    }

    @Test
    public void testPatchProduct_productNotFound() throws Exception {

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("barcode", "nonexistentBarcode");
        updateRequest.put("name", "Updated Name");
        updateRequest.put("buyPrice", 699);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product with the specified barcode does not exist."));
    }

    @Test
    public void testPatchProduct_nonIntegerBuyPrice() throws Exception {
        repository.save(validProduct);

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("barcode", "1234");
        updateRequest.put("buyPrice", 4.32);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Buy Price must be an integer."));
    }

    @Test
    public void testPatchProduct_negativeBuyPrice() throws Exception {
        repository.save(validProduct);

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("barcode", "1234");
        updateRequest.put("buyPrice", -400);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Buy Price must be a positive integer."));
    }


    //---------------------------------GET ENDPOINT---------------------------------//
    @Test
    public void testGetAllProducts() throws Exception {
        repository.save(validProduct);

        Product validProduct2 = new Product("5678", "test2", 599);
        repository.save(validProduct2);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/product")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].barcode").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].barcode").value("5678"));
    }

    @Test
    public void testGetOneProduct_productExists() throws Exception {
        repository.save(validProduct);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/product/1234")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.barcode").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyPrice").value(599));
    }

    @Test
    public void testGetOneProduct_productDoesNotExist() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/product/1234")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product not found."));

    }


    //---------------------------------DELETE ENDPOINT---------------------------------//
    @Test
    public void testDeleteProduct_productExists() throws Exception {
        repository.save(validProduct);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/product/1234")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteProduct_productDoesNotExist() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/product/1234")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product not found."));
    }



}
