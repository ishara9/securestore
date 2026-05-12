package com.example.store.service.impl;

import com.example.store.dto.CreateOrderRequestDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.PageResponse;
import com.example.store.entity.Order;
import com.example.store.exceptions.ItemNotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.OrderRepository;
import com.example.store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDTO createOrder(CreateOrderRequestDTO orderDTO) {
        return orderMapper.orderToOrderDTO(orderRepository.save(orderMapper.orderDTOtoOrder(orderDTO)));
    }

    @Override
    public PageResponse<OrderDTO> getAllOrders(Pageable pageable) {
        return orderMapper.ordersToOrderDTOsPage(orderRepository.findAllWithProducts(pageable));
    }

    @Override
    public OrderDTO findOrderById(Long id) {
        Order order = orderRepository.findByIdWithProducts(id).orElseThrow(
                () -> new ItemNotFoundException(String.format("Order with id %d not found", id)));
        return orderMapper.orderToOrderDTO(order);
    }
}
