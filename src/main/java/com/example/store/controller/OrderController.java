package com.example.store.controller;

import com.example.store.dto.CreateOrderRequestDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.PageResponse;
import com.example.store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "orders", allEntries = true)
    public OrderDTO createOrder(@RequestBody CreateOrderRequestDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    @GetMapping
    @Cacheable(
            value = "orders",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort"
    )
    public PageResponse<OrderDTO> getAllOrders(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/{id}")
    public OrderDTO findOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }
}
