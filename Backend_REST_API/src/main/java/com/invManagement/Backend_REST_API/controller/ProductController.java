package com.invManagement.Backend_REST_API.controller;


import com.invManagement.Backend_REST_API.model.Product;
import com.invManagement.Backend_REST_API.model.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    /*
    Endpoint to add new products.
    All product fields must be in the request body.
    Product with same barcode must not already exist.
     */
    @PostMapping
    public ResponseEntity<?> postProduct(@RequestBody Product product) {

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

        if (repository.existsById(product.getBarcode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Product with the same barcode already exists.");
        }

        Product savedProduct = repository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    /*
    Endpoint to update the fields of an existing product.
    Request body must contain the product barcode, and the information to update.
     */
    @PatchMapping
    public ResponseEntity<?> patchProduct(@RequestBody Map<String, Object> updates) {

        // Check if barcode is present
        if (!updates.containsKey("barcode") || updates.get("barcode") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Barcode is required to identify the product.");
        }

        String barcode = updates.get("barcode").toString();

        // Find the product by barcode
        Product oldProduct = repository.findByBarcode(barcode);
        if (oldProduct == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with the specified barcode does not exist.");
        }

        // Update the fields
        if (updates.containsKey("name") && updates.get("name") != null) {
            oldProduct.setName(updates.get("name").toString());
        }
        if (updates.containsKey("buyPrice") && updates.get("buyPrice") != null) {
            try {
                int buyPrice = Integer.parseInt(updates.get("buyPrice").toString());
                if (buyPrice < 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Buy Price must be a positive integer.");
                }
                oldProduct.setBuyPrice(buyPrice);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Buy Price must be an integer.");
            }
        }

        // Save the updated product
        Product updatedProduct = repository.save(oldProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    /*
    Retrieve all products.
     */
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = repository.findAll();
        return  ResponseEntity.ok(products);
    }

    /*
    Get an individual product.
     */
    @GetMapping("/{barcode}")
    public ResponseEntity<?> getProduct(@PathVariable String barcode) {
        Product product = repository.findByBarcode(barcode);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{barcode}")
    public ResponseEntity<?> deleteProduct(@PathVariable String barcode) {
        if (!repository.existsById(barcode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        repository.deleteById(barcode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
