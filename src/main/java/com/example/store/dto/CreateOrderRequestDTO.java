package com.example.store.dto;

import java.util.List;

public record CreateOrderRequestDTO(
        String description, CustomerDTO customer, List<ProductDTO> products
) {
}
