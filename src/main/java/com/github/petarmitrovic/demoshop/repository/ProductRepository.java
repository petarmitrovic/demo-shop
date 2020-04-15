package com.github.petarmitrovic.demoshop.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.petarmitrovic.demoshop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySkuAndActive(UUID sku, boolean active);

    default Optional<Product> findActiveBySku(String sku) {
        return findBySkuAndActive(UUID.fromString(sku), true);
    }
}
