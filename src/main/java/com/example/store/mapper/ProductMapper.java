package com.example.store.mapper;

import com.example.store.dto.CreateProductRequestDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {Order.class})
public interface ProductMapper extends PageMapper<Product, ProductDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    Product createProductRequestDTOToProductDTO(CreateProductRequestDTO createProductRequestDTO);

    @Mapping(target = "orderIds", expression = "java(product.getOrders().stream().map(Order::getId).toList())")
    ProductDTO productToProductDTO(Product product);
}
