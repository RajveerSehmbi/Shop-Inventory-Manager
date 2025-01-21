package com.invManagement.Backend_REST_API.models.Inventory;

import com.invManagement.Backend_REST_API.models.Product.Product;
import com.invManagement.Backend_REST_API.models.Shop.Shop;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "inventory", uniqueConstraints = @UniqueConstraint(columnNames = {"shop_id", "barcode"}))
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "barcode", nullable = false)
    private Product product;

    @Digits(integer = 10, fraction = 0, message = "Quantity must be an integer.")
    @Min(value = 0, message = "Quantity must be a positive integer.")
    private Integer quantity;

    public Inventory(Long id, Shop shop, Product product, Integer quantity) {
        this.id = id;
        this.shop = shop;
        this.product = product;
        this.quantity = quantity;
    }

    public Inventory(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
