package com.github.petarmitrovic.demoshop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductDto {

    private String sku;
    private String name;
    private LocalDate createdOn;
    private BigDecimal price;

    public ProductDto() {
    }

    public ProductDto(String sku, String name, LocalDate createdOn, BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.createdOn = createdOn;
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
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
}
