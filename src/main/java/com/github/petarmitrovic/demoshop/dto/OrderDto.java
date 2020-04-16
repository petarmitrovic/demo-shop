package com.github.petarmitrovic.demoshop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Order")
public class OrderDto {
    @ApiModelProperty("A unique identifier of the order")
    private String uniqueId;

    @ApiModelProperty("An email of a buyer")
    private String buyer;

    @ApiModelProperty("Represent the time when the order was placed")
    private LocalDateTime placedAt;

    @ApiModelProperty("A unique identifier of an order")
    private List<ItemDto> items;

    @ApiModelProperty("Total amount of this order")
    private BigDecimal total;

    public OrderDto() {
    }

    public OrderDto(String uniqueId, String buyer, LocalDateTime placedAt, List<ItemDto> items, BigDecimal total) {
        this.uniqueId = uniqueId;
        this.buyer = buyer;
        this.placedAt = placedAt;
        this.items = items;
        this.total = total;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public LocalDateTime getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(LocalDateTime placedAt) {
        this.placedAt = placedAt;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @ApiModel("An order item")
    public static class ItemDto {

        @ApiModelProperty("The sku of the product associated with this item")
        private String sku;
        @ApiModelProperty("The name of the product associated with this item")
        private String name;
        @ApiModelProperty("The amount")
        private Integer amount;
        @ApiModelProperty("The price of the product at the time the order was placed")
        private BigDecimal price;

        public ItemDto() {
        }

        public ItemDto(String sku, String name, Integer amount, BigDecimal price) {
            this.sku = sku;
            this.name = name;
            this.amount = amount;
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

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
