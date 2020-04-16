package com.github.petarmitrovic.demoshop.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.petarmitrovic.demoshop.dto.OrderDto;
import com.github.petarmitrovic.demoshop.entity.Order;
import com.github.petarmitrovic.demoshop.entity.Product;
import com.github.petarmitrovic.demoshop.repository.OrderRepository;
import com.github.petarmitrovic.demoshop.repository.ProductRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrdersControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    public void setupData() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        productRepository.saveAll(List.of(
            new Product(UUID.fromString("245b6b69-8bd2-4c60-876e-ac81f60d9405"), "Product 1", LocalDate.of(2020, 4, 14), new BigDecimal("13.34")),
            new Product(UUID.fromString("f914eeb9-a6f3-4950-9c5a-4a83f1b66656"), "Product 2", LocalDate.of(2020, 4, 16), new BigDecimal("17.95"))
        ));
    }

    @Test
    public void itShouldPlaceAnOrder() throws Exception {
        given()
            .body(readFile("/newOrder.json"))
            .contentType(ContentType.JSON)
            .post("/orders")
        .then()
            .statusCode(201);

        Order saved = orderRepository.findAll().get(0);

        assertEquals("someone@email.com", saved.getBuyersEmail());
        assertEquals(2, saved.getItems().size());
    }

    @Test
    public void itShouldFailIfEmailIsNotValid() throws Exception {
        given()
            .body(readFile("/newOrder_InvalidEmail.json"))
            .contentType(ContentType.JSON)
            .post("/orders")
        .then()
            .statusCode(400);
    }

    @Test
    public void itShouldFetchOrders() throws Exception {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        given()
            .body(readFile("/newOrder.json"))
            .contentType(ContentType.JSON)
            .post("/orders");

        OrderDto[] orders = given()
            .get("/orders?from=" + startOfDay+ "&to=" + endOfDay)
        .then()
            .statusCode(200)
            .extract()
            .response()
            .as(OrderDto[].class);
        assertEquals(1, orders.length);

        OrderDto order = orders[0];
        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("44.63"), order.getTotal());

        assertEquals("Product 1", order.getItems().get(0).getName());
        assertEquals("245b6b69-8bd2-4c60-876e-ac81f60d9405", order.getItems().get(0).getSku());
    }

    private String readFile(String file) throws Exception {
        return Files.readString(Paths.get(this.getClass().getResource(file).toURI()));
    }
}
