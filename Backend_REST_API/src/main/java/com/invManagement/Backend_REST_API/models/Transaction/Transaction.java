package com.invManagement.Backend_REST_API.models.Transaction;

import com.invManagement.Backend_REST_API.models.Product.Product;
import com.invManagement.Backend_REST_API.models.Shop.Shop;
import com.invManagement.Backend_REST_API.models.User.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User user;

    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionItem> items;

    public Transaction() {}

    public Transaction(Shop shop, User user, LocalDateTime dateTime, List<TransactionItem> items) {
        this.shop = shop;
        this.user = user;
        this.dateTime = dateTime;
        this.items = items;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<TransactionItem> getItems() {
        return items;
    }

    public void setItems(List<TransactionItem> items) {
        this.items = items;
    }

}
