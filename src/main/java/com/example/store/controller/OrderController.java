package com.example.store.controller;

import com.example.store.dto.CreateOrderRequestDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "orders", allEntries = true)
    public OrderDTO createOrder(@Valid @RequestBody CreateOrderRequestDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    @Cacheable(
            value = "orders",
            key = "'order-search'"
    )
    @GetMapping
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDTO findOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }
}
