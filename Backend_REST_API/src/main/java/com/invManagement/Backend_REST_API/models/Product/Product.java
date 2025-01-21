package com.invManagement.Backend_REST_API.models.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Product {


    @Id
    @NotEmpty(message = "Barcode is required.")
    private String barcode;

    @NotEmpty(message = "Name is required.")
    private String name;

    @NotNull(message = "Buy Price is required.")
    @Digits(integer = 10, fraction = 0, message = "Buy Price must be an integer.")
    @Min(value = 0, message = "Buy Price must be a positive integer.")
    private Integer buyPrice;

    // Constructors
    public Product(){}

    public Product(String barcode, String name, Integer buyPrice) {
        this.barcode = barcode;
        this.name = name;
        this.buyPrice = buyPrice;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuyPrice(Integer price) {
        this.buyPrice = price;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public Integer getBuyPrice() {
        return buyPrice;
    }
}
