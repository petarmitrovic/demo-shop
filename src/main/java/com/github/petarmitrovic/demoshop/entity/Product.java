package com.github.petarmitrovic.demoshop.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private UUID sku;
    @Column
    private String name;
    @Column
    private LocalDate createdOn;
    private BigDecimal price;
    private boolean active = true;

    public Product() { }

    public Product(UUID sku, String name, LocalDate createdOn, BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.createdOn = createdOn;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getSku() {
        return sku;
    }

    public void setSku(UUID sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
