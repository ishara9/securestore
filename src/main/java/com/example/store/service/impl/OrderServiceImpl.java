package com.example.store.service.impl;

import com.example.store.dto.CreateOrderRequestDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.PageResponse;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.exceptions.EntityNotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import com.example.store.service.OrderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderRequestDTO request) {

        Long customerId = request.customer().getId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Customer with id: %d not found", customerId))
                );

        List<Product> products = productRepository.findAllById(
                request.products().stream().map(ProductDTO::id).toList()
        );

        if(products.size() != request.products().size()){
            throw new EntityNotFoundException("One or more products not found");
        }

        Order order = new Order();
        order.setDescription(request.description());
        order.setCustomer(customer);
        order.setProducts(products);

        Order saved = orderRepository.save(order);

        Order fetched = orderRepository.findByIdWithDetails(saved.getId())
                .orElseThrow();

        return orderMapper.orderToOrderDTO(fetched);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.ordersToOrderDTOs(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderDTO> getAllOrders(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<Order> withDetailsByIds = orderRepository.findWithDetailsByIds(
                orderPage.getContent().stream().map(Order::getId).toList()
        );
        return orderMapper.ordersToOrderDTOsPage(orderPage, withDetailsByIds);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO findOrderById(Long id) {
        Order order = orderRepository.findByIdWithDetails(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Order with id %d not found", id)));
        return orderMapper.orderToOrderDTO(order);
    }
}
