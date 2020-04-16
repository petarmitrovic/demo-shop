package com.github.petarmitrovic.demoshop.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.petarmitrovic.demoshop.dto.OrderCommand;
import com.github.petarmitrovic.demoshop.dto.OrderDto;
import com.github.petarmitrovic.demoshop.service.OrdersService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * An implementation of "orders" endpoint
 */
@RestController
@RequestMapping("/orders")
@Api(value = "Orders", description = "Operations pertaining to orders in Demo Shop", tags = "Orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Places a new order and returns its unique id as a response.", response = String.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Successfully created a new product."),
        @ApiResponse(code = 400, message = "In case of invalid data submitted."),
    })
    String placeOrder(@Valid @RequestBody OrderCommand command) {
        return this.ordersService.placeOrder(command);
    }

    @ApiOperation(value = "Fetches a list of orders for a given period of time.", response = List.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successfully retrieved the list")
    })
    @GetMapping
    List<OrderDto> fetch(
            @ApiParam(value = "Start of the filter period", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @ApiParam(value = "End of the filter period", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return this.ordersService.fetchOrders(from, to);
    }
}
