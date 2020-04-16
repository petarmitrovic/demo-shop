package com.github.petarmitrovic.demoshop.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.petarmitrovic.demoshop.dto.ProductDto;
import com.github.petarmitrovic.demoshop.entity.Product;
import com.github.petarmitrovic.demoshop.repository.ProductRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.path.json.config.JsonPathConfig;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    public void setupData() {
        productRepository.deleteAll();
        productRepository.saveAll(List.of(
                new Product(UUID.fromString("245b6b69-8bd2-4c60-876e-ac81f60d9405"), "Product 1", LocalDate.of(2020, 4, 14), new BigDecimal("13.34")),
                new Product(UUID.fromString("f914eeb9-a6f3-4950-9c5a-4a83f1b66656"), "Product 2", LocalDate.of(2020, 4, 16), new BigDecimal("17.95")),
                new Product(UUID.fromString("909b1d2d-bc28-416a-9d75-bd2798b206c8"), "Product 3", LocalDate.of(2020, 4, 13), new BigDecimal("11.00")),
                new Product(UUID.fromString("792127a4-a0ac-4168-90ba-e76b9cded642"), "Product 4", LocalDate.of(2020, 4, 15), new BigDecimal("21.50"))
        ));
    }

    @Test
    public void itShouldFetchAllProducts() {
        given()
            .config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)))
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body("sku", hasItems("245b6b69-8bd2-4c60-876e-ac81f60d9405", "f914eeb9-a6f3-4950-9c5a-4a83f1b66656", "909b1d2d-bc28-416a-9d75-bd2798b206c8", "792127a4-a0ac-4168-90ba-e76b9cded642"))
            .body("name", hasItems("Product 1", "Product 2", "Product 3", "Product 4"))
            .body("createdOn", hasItems("2020-04-14", "2020-04-16", "2020-04-13", "2020-04-15"))
            .body("price", hasItems(new BigDecimal("13.34"), new BigDecimal("17.95"), new BigDecimal("11.00"), new BigDecimal("21.50")));
    }

    @Test
    public void itShouldFetchSingleProduct() {
        given()
            .config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)))
        .when()
            .get("/products/f914eeb9-a6f3-4950-9c5a-4a83f1b66656")
        .then()
            .statusCode(200)
            .body("sku", equalTo("f914eeb9-a6f3-4950-9c5a-4a83f1b66656"))
            .body("name", equalTo("Product 2"))
            .body("createdOn", equalTo("2020-04-16"))
            .body("price", is(new BigDecimal("17.95")));
    }

    @Test
    public void itShouldReturn404WhenNoProductWithGiven() {
        given()
            .config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL)))
        .when()
            .get("/products/11111111-1111-1111-1111-111111111111")
        .then()
            .statusCode(404);
    }

    @Test
    public void itShouldAddNewProduct() throws Exception {
        ProductDto createdProduct = given()
            .body(readFile("/newProduct.json"))
            .contentType(ContentType.JSON)
            .post("/products")
        .then()
            .statusCode(201)
            .extract()
            .response()
            .body()
            .as(ProductDto.class);

        assertEquals(5, productRepository.count());

        Product product = productRepository.findActiveBySku(createdProduct.getSku()).get();

        assertEquals("New product", product.getName());
        assertEquals(new BigDecimal("13.70"), product.getPrice());
    }

    @Test
    public void itShouldUpdateProduct() throws Exception {
        given()
            .body(readFile("/editProduct.json"))
            .contentType(ContentType.JSON)
            .put("/products")
        .then()
            .statusCode(200);

        Product updatedProduct = productRepository.findActiveBySku("909b1d2d-bc28-416a-9d75-bd2798b206c8").get();
        assertEquals("Updated product", updatedProduct.getName());
        assertEquals(new BigDecimal("15.10"), updatedProduct.getPrice());
    }

    @Test
    public void itShouldDeleteProduct() throws Exception {
        given()
            .delete("/products/792127a4-a0ac-4168-90ba-e76b9cded642")
        .then()
            .statusCode(200);

        assertEquals(3, productRepository.count());

        Optional<Product> product = productRepository.findActiveBySku("792127a4-a0ac-4168-90ba-e76b9cded642");
        assertFalse(product.isPresent());
    }

    private String readFile(String file) throws Exception {
        return Files.readString(Paths.get(this.getClass().getResource(file).toURI()));
    }
}
