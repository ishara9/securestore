package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.dto.PageResponse;
import com.example.store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/order")
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderService orderService;

    @GetMapping
    @Cacheable(
            value = "orders",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort"
    )
    public PageResponse<OrderDTO> getAllOrders(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }
}
