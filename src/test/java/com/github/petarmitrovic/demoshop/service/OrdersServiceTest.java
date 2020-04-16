package com.github.petarmitrovic.demoshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.petarmitrovic.demoshop.dto.OrderCommand;
import com.github.petarmitrovic.demoshop.dto.OrderDto;
import com.github.petarmitrovic.demoshop.entity.Order;
import com.github.petarmitrovic.demoshop.entity.Product;
import com.github.petarmitrovic.demoshop.repository.OrderRepository;
import com.github.petarmitrovic.demoshop.repository.ProductRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OrdersServiceTest {

    private static final String SKU_1 = "f04917ec-ed2f-4f27-8322-d736a1a2a69a";
    private static final String SKU_2 = "fd0bb09b-ed98-461c-9077-19bf36c02e11";
    private static final String SKU_3 = "bb7675a7-1d47-47c3-b30d-3deded6335a0";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrdersService ordersService;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    public void itShouldPlaceNewOrder() {
        // given
        productRepository.saveAll(List.of(
            new Product(UUID.fromString(SKU_1), "Product 1", LocalDate.now(), new BigDecimal("14.50")),
            new Product(UUID.fromString(SKU_2), "Product 2", LocalDate.now(), new BigDecimal("9.80")),
            new Product(UUID.fromString(SKU_3), "Product 3", LocalDate.now(), new BigDecimal("19.99"))
        ));

        // when
        ordersService.placeOrder(new OrderCommand("some@email.com", List.of(
                new OrderCommand.Item(SKU_1, 1),
                new OrderCommand.Item(SKU_2, 2),
                new OrderCommand.Item(SKU_3, 1)
        )));

        // then
        Order savedOrder = orderRepository.findAll().get(0);
        
        assertEquals("some@email.com", savedOrder.getBuyersEmail());
        assertEquals(3, savedOrder.getItems().size());

        assertEquals("Product 1", savedOrder.getItems().get(0).getName());
        assertEquals(1, savedOrder.getItems().get(0).getAmount());
        assertEquals(new BigDecimal("14.50"), savedOrder.getItems().get(0).getPrice());
    }

    @Test
    public void itShouldFetchOrders() {
        // given
        productRepository.saveAll(List.of(
                new Product(UUID.fromString(SKU_1), "Product 1", LocalDate.now(), new BigDecimal("14.50")),
                new Product(UUID.fromString(SKU_2), "Product 2", LocalDate.now(), new BigDecimal("9.80")),
                new Product(UUID.fromString(SKU_3), "Product 3", LocalDate.now(), new BigDecimal("19.99"))
        ));

        String order1Id = ordersService.placeOrder(new OrderCommand("mail_1@email.com", List.of(
                new OrderCommand.Item(SKU_1, 1),
                new OrderCommand.Item(SKU_2, 2)
        )));

        String order2Id = ordersService.placeOrder(new OrderCommand("mail_2@email.com", List.of(
                new OrderCommand.Item(SKU_2, 2),
                new OrderCommand.Item(SKU_3, 1)
        )));

        LocalDateTime startOfTheDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endOfTheDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        // when
        List<OrderDto> orders = ordersService.fetchOrders(startOfTheDay, endOfTheDay);

        // then
        assertEquals(2, orders.size());

        OrderDto order2 = orders.stream().filter(order -> order.getUniqueId().equals(order2Id)).findFirst().get();

        assertEquals("Product 2", order2.getItems().get(0).getName());
        assertEquals(2, order2.getItems().get(0).getAmount());
        assertEquals(new BigDecimal("9.80"), order2.getItems().get(0).getPrice());

        assertEquals("Product 3", order2.getItems().get(1).getName());
        assertEquals(1, order2.getItems().get(1).getAmount());
        assertEquals(new BigDecimal("19.99"), order2.getItems().get(1).getPrice());

        assertEquals(new BigDecimal("39.59"), order2.getTotal());
    }
}
