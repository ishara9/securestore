package com.example.store.service;

import com.example.store.dto.CreateOrderRequestDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(CreateOrderRequestDTO orderDTO);

    List<OrderDTO> getAllOrders();

    PageResponse<OrderDTO> getAllOrders(Pageable pageable);

    OrderDTO findOrderById(Long id);
}
