package com.invManagement.Backend_REST_API.models.Shop;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Shop {

    @Id
    @NotEmpty(message = "Shop ID is required.")
    private String id;

    @NotEmpty(message = "Name is required.")
    private String name;

    private String address;


    public Shop(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Shop() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
