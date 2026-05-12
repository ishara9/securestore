package com.example.store.service;

import com.example.store.dto.CreateOrderRequestDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDTO createOrder(CreateOrderRequestDTO orderDTO);

    PageResponse<OrderDTO> getAllOrders(Pageable pageable);

    OrderDTO findOrderById(Long id);
}
