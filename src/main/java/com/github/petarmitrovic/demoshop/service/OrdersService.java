package com.github.petarmitrovic.demoshop.service;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.github.petarmitrovic.demoshop.dto.OrderCommand;
import com.github.petarmitrovic.demoshop.dto.OrderDto;
import com.github.petarmitrovic.demoshop.entity.Item;
import com.github.petarmitrovic.demoshop.entity.Order;
import com.github.petarmitrovic.demoshop.repository.OrderRepository;
import com.github.petarmitrovic.demoshop.repository.ProductRepository;

@Service
public class OrdersService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrdersService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public String placeOrder(OrderCommand orderCommand) {
        Order order = new Order();
        order.setBuyersEmail(orderCommand.getEmail());
        order.setUniqueId(UUID.randomUUID());
        order.setPlacedAt(LocalDateTime.now());

        List<Item> items = new ArrayList<>(orderCommand.getItems().size());
        orderCommand.getItems()
            .forEach(item -> {
                productRepository.findActiveBySku(item.getProduct())
                    .ifPresentOrElse(product -> {
                        Item orderItem = new Item();
                        orderItem.setSku(product.getSku());
                        orderItem.setName(product.getName());
                        orderItem.setPrice(product.getPrice());
                        orderItem.setAmount(item.getAmount());
                        items.add(orderItem);
                    },
                    () -> {
                        throw new NoSuchElementException("Product not found: " + item.getProduct());
                    });
            });

        order.setItems(items);
        return this.orderRepository.save(order)
                .getUniqueId().toString();
    }

    public List<OrderDto> fetchOrders(LocalDateTime from, LocalDateTime to) {
        return this.orderRepository.findAllByPlacedAtBetween(from, to)
            .stream()
            .map(this::toDto)
            .collect(toList());
    }

    private OrderDto toDto(Order order) {
        return new OrderDto(
            order.getUniqueId().toString(),
            order.getBuyersEmail(),
            order.getPlacedAt(),
            order.getItems().stream().map(this::toDto).collect(toList()),
            order.getItems().stream().map(Item::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private OrderDto.ItemDto toDto(Item item) {
        return new OrderDto.ItemDto(item.getSku().toString(), item.getName(), item.getAmount(), item.getPrice());
    }
}
