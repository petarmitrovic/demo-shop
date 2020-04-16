package com.github.petarmitrovic.demoshop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.petarmitrovic.demoshop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByPlacedAtBetween(LocalDateTime from, LocalDateTime to);
}
