package com.example.store.mapper;

import com.example.store.dto.*;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper extends PageMapper<Order, OrderDTO> {
    @Mapping(target = "products", qualifiedByName = "productToDTO")
    OrderDTO orderToOrderDTO(Order order);

    @Named("productToDTO")
    @Mapping(target = "orderIds", ignore = true)
    ProductDTO productToProductDTO(Product product);

    OrderCustomerDTO orderToOrderCustomerDTO(Customer customer);

    List<OrderDTO> ordersToOrderDTOs(List<Order> order);

    default PageResponse<OrderDTO> ordersToOrderDTOsPage(Page<Order> page, List<Order> hydratedOrders) {
        Map<Long, Order> hydratedMap = hydratedOrders.stream()
                .collect(Collectors.toMap(Order::getId, o -> o));

        return mapPage(page, o -> orderToOrderDTO(hydratedMap.getOrDefault(o.getId(), o)));
    }
}
