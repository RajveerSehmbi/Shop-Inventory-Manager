package com.invManagement.Backend_REST_API.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {


    @Id
    private String barcode;
    private String name;
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
