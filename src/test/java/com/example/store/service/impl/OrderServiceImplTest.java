package com.example.store.service.impl;

import com.example.store.dto.CreateOrderRequestDTO;
import com.example.store.dto.CustomerDTO;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testCreateOrder_success() {
        CreateOrderRequestDTO request = new CreateOrderRequestDTO(
                "new order",
                customerDTO(1L),
                List.of(new ProductDTO(10L, "P1", List.of()), new ProductDTO(20L, "P2", List.of()))
        );
        Customer customer = new Customer();
        customer.setId(1L);
        Product product1 = new Product();
        product1.setId(10L);
        Product product2 = new Product();
        product2.setId(20L);

        Order saved = new Order();
        saved.setId(100L);
        Order fetched = new Order();
        fetched.setId(100L);
        OrderDTO mapped = new OrderDTO();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findAllById(List.of(10L, 20L))).thenReturn(List.of(product1, product2));
        when(orderRepository.save(any(Order.class))).thenReturn(saved);
        when(orderRepository.findByIdWithDetails(100L)).thenReturn(Optional.of(fetched));
        when(orderMapper.orderToOrderDTO(fetched)).thenReturn(mapped);

        OrderDTO result = orderService.createOrder(request);

        assertSame(mapped, result);
        verify(customerRepository).findById(1L);
        verify(productRepository).findAllById(List.of(10L, 20L));
        verify(orderRepository).save(any(Order.class));
        verify(orderRepository).findByIdWithDetails(100L);
    }

    @Test
    void testCreateOrder_whenCustomerNotFound_throwsException() {
        CreateOrderRequestDTO request = new CreateOrderRequestDTO(
                "new order",
                customerDTO(1L),
                List.of(new ProductDTO(10L, "P1", List.of()))
        );
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(request));
        verify(customerRepository).findById(1L);
    }

    @Test
    void testCreateOrder_whenProductsNotFound_throwsException() {
        CreateOrderRequestDTO request = new CreateOrderRequestDTO(
                "new order",
                customerDTO(1L),
                List.of(new ProductDTO(10L, "P1", List.of()), new ProductDTO(20L, "P2", List.of()))
        );
        Customer customer = new Customer();
        customer.setId(1L);
        Product product = new Product();
        product.setId(10L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findAllById(List.of(10L, 20L))).thenReturn(List.of(product));

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(request));
        verify(productRepository).findAllById(List.of(10L, 20L));
    }

    @Test
    void testGetAllOrders_returnsMappedList() {
        List<Order> orders = List.of(new Order());
        List<OrderDTO> mapped = List.of(new OrderDTO());

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.ordersToOrderDTOs(orders)).thenReturn(mapped);

        List<OrderDTO> result = orderService.getAllOrders();

        assertSame(mapped, result);
        verify(orderRepository).findAll();
    }

    @Test
    void testGetAllOrdersPageable_returnsMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Order order = new Order();
        order.setId(1L);
        Page<Order> orderPage = new PageImpl<>(List.of(order), pageable, 1L);
        List<Order> hydrated = List.of(order);
        PageResponse<OrderDTO> response = new PageResponse<>(List.of(new OrderDTO()), 0, 10, 1L);

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderRepository.findWithDetailsByIds(List.of(1L))).thenReturn(hydrated);
        when(orderMapper.ordersToOrderDTOsPage(orderPage, hydrated)).thenReturn(response);

        PageResponse<OrderDTO> result = orderService.getAllOrders(pageable);

        assertSame(response, result);
        verify(orderRepository).findAll(pageable);
        verify(orderRepository).findWithDetailsByIds(List.of(1L));
        verify(orderMapper).ordersToOrderDTOsPage(eq(orderPage), eq(hydrated));
    }

    @Test
    void testFindOrderById_returnsMappedOrder() {
        Order order = new Order();
        OrderDTO dto = new OrderDTO();

        when(orderRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDTO(order)).thenReturn(dto);

        OrderDTO result = orderService.findOrderById(1L);

        assertSame(dto, result);
        verify(orderRepository).findByIdWithDetails(1L);
    }

    @Test
    void testFindOrderById_whenNotFound_throwsException() {
        when(orderRepository.findByIdWithDetails(55L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.findOrderById(55L));
        verify(orderRepository).findByIdWithDetails(55L);
    }

    private CustomerDTO customerDTO(Long id) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(id);
        return customerDTO;
    }
}