package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.dto.PageResponse;
import com.example.store.entity.Order;
import com.example.store.exceptions.ItemNotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;
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

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @CacheEvict(value = "orders", allEntries = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@RequestBody Order order) {
        return orderMapper.orderToOrderDTO(orderRepository.save(order));
    }

    @GetMapping
    @Cacheable(
            value = "orders",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort"
    )
    public PageResponse<OrderDTO> getAllOrders(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return orderMapper.ordersToOrderDTOsPage(orderRepository.findAllWithProducts(pageable));
    }

    @GetMapping("/{id}")
    public OrderDTO findOrderById(@PathVariable Long id) {
        Order order = orderRepository.findByIdWithProducts(id).orElseThrow(() -> new ItemNotFoundException(String.format("Order with id %d not found", id)));
        return orderMapper.orderToOrderDTO(order);
    }
}
