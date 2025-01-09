package com.invManagement.Backend_REST_API.controller;


import com.invManagement.Backend_REST_API.model.Product;
import com.invManagement.Backend_REST_API.model.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/product/add")
    public ResponseEntity<?> addNewProduct(@RequestBody Product product) {

        List<String> errors = new ArrayList<>();

        if (product.getBarcode() == null || product.getBarcode().isEmpty()) {
            errors.add("Barcode is required.");
        }
        if (product.getName() == null || product.getName().isEmpty()) {
            errors.add("Name is required.");
        }
        if (product.getBuyPrice() == null) {
            errors.add("Buy Price is required.");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.join(" ", errors));
        }

        Product savedProduct = repository.save(product);
        return ResponseEntity.ok(savedProduct);
    }


}
