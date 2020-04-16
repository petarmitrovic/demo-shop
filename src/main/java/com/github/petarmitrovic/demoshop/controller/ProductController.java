package com.github.petarmitrovic.demoshop.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.petarmitrovic.demoshop.dto.ProductCommand;
import com.github.petarmitrovic.demoshop.dto.ProductDto;
import com.github.petarmitrovic.demoshop.entity.Product;
import com.github.petarmitrovic.demoshop.repository.ProductRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * An implementation of /products endpoint.
 */
@RestController
@RequestMapping("/products")
@Api(value = "Products", description = "Operations pertaining to products in Demo Shop", tags = "Products")
class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    @ApiOperation(value = "Fetches a list of all products.", response = List.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successfully retrieved the list")
    })
    List<ProductDto> fetchAll() {
        return this.productRepository.findAll()
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/{sku}")
    @ApiOperation(value = "Fetches a single products by given sku.", response = ProductDto.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successfully retrieved the product."),
        @ApiResponse(code = 400, message = "Product with the given sku not found.")
    })
    ResponseEntity<ProductDto> getSingle(@PathVariable String sku) {
        return productRepository.findActiveBySku(sku)
                .map(product -> ResponseEntity.ok(toDto(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Creates a new product and returns it as a response.", response = ProductDto.class)
    @ApiResponses({
        @ApiResponse(code = 201, message = "Successfully created a new product."),
    })
    ResponseEntity<ProductDto> create(@RequestBody ProductCommand command) {
        Product savedProduct = this.productRepository.save(
                new Product(UUID.randomUUID(), command.getName(), LocalDate.now(), command.getPrice())
        );
        return ResponseEntity.created(URI.create("/products/" + savedProduct.getSku()))
                .body(toDto(savedProduct));
    }

    @PutMapping
    @ApiOperation(value = "Updates the product.", response = ProductDto.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successfully updated the product."),
        @ApiResponse(code = 404, message = "Referenced product not found."),
    })
    ResponseEntity<ProductDto> update(@RequestBody ProductCommand command) {
        return this.productRepository.findActiveBySku(command.getSku())
                .map(prod -> {
                    prod.setName(command.getName());
                    prod.setPrice(command.getPrice());
                    return ResponseEntity.ok(toDto(productRepository.save(prod)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{sku}")
    @ApiOperation(value = "Deletes the product with given sku.", response = ProductDto.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Successfully deleted the product."),
        @ApiResponse(code = 404, message = "Referenced product not found."),
    })
    ResponseEntity<String> delete(@PathVariable String sku) {
        Optional<Product> found = this.productRepository.findActiveBySku(sku);
        if (found.isPresent()) {
            this.productRepository.delete(found.get());
            return ResponseEntity.ok(sku);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ProductDto toDto(Product entity) {
        return new ProductDto(entity.getSku().toString(), entity.getName(), entity.getCreatedOn(), entity.getPrice());
    }
}
