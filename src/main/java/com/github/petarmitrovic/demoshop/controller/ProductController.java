package com.github.petarmitrovic.demoshop.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.petarmitrovic.demoshop.dto.ProductCommand;
import com.github.petarmitrovic.demoshop.dto.ProductDto;
import com.github.petarmitrovic.demoshop.entity.Product;
import com.github.petarmitrovic.demoshop.repository.ProductRepository;

/**
 * An implementation of /products endpoint.
 */
@RestController
@RequestMapping("/products")
class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    List<ProductDto> fetchAll() {
        return this.productRepository.findAll()
                .stream()
                .map(entity -> new ProductDto(entity.getSku().toString(), entity.getName(), entity.getCreatedOn(), entity.getPrice()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{sku}")
    ResponseEntity<Product> getSingle(@PathVariable String sku) {
        return productRepository.findActiveBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    String create(@RequestBody ProductCommand command) {
        Product savedProduct = this.productRepository.save(
                new Product(UUID.randomUUID(), command.getName(), LocalDate.now(), command.getPrice())
        );
        return savedProduct.getSku().toString();
    }

    @DeleteMapping("/{sku}")
    ResponseEntity<String> delete(@PathVariable String sku) {
        Optional<Product> found = this.productRepository.findActiveBySku(sku);
        if (found.isPresent()) {
            this.productRepository.delete(found.get());
            return ResponseEntity.ok(sku);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
