package com.example.store.mapper;

import com.example.store.dto.CreateOrderRequestDTO;
import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.PageResponse;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface OrderMapper extends PageMapper<Order, OrderDTO> {
    OrderDTO orderToOrderDTO(Order order);

    OrderCustomerDTO orderToOrderCustomerDTO(Customer customer);

    Order orderDTOtoOrder(CreateOrderRequestDTO orderDTO);

    default PageResponse<OrderDTO> ordersToOrderDTOsPage(Page<Order> orders) {
        return mapPage(orders, this::orderToOrderDTO);
    }
}
