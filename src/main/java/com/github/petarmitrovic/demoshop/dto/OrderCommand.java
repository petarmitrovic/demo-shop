package com.github.petarmitrovic.demoshop.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "A model used to pass information when placing an order.")
public class OrderCommand {

    @ApiModelProperty("Buyer's email")
    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    @ApiModelProperty("A list of items associated with this order")
    private List<Item> items;

    public OrderCommand() {
    }

    public OrderCommand(String email, List<Item> items) {
        this.email = email;
        this.items = items;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @ApiModel("Order item")
    public static class Item {

        @ApiModelProperty("SKA of a product associated with this item")
        private String product;

        @ApiModelProperty("Amount of products associated with this item")
        private Integer amount;

        public Item() {
        }

        public Item(String product, Integer amount) {
            this.product = product;
            this.amount = amount;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }
    }
}
