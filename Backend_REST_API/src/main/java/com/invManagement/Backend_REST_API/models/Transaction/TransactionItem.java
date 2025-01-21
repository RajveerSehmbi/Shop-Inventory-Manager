package com.invManagement.Backend_REST_API.models.Transaction;

import com.invManagement.Backend_REST_API.models.Product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;

@Entity
public class TransactionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Digits(integer = 10, fraction = 0, message = "Quantity must be an integer.")
    private Integer quantity;

    public TransactionItem() {}

    public TransactionItem(Transaction transaction, Product product, Integer quantity) {
        this.transaction = transaction;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
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
