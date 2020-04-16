package com.github.petarmitrovic.demoshop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Product", description = "Product details")
public class ProductDto {

    @ApiModelProperty(notes = "Stock keeping unit, a unique id of the product.")
    private String sku;

    @ApiModelProperty(notes = "Product name")
    private String name;

    @ApiModelProperty(notes = "Product price")
    private BigDecimal price;

    @ApiModelProperty(notes = "Product creation date")
    private LocalDate createdOn;

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
