package com.example.store.dto;

import java.util.List;

public record CreateOrderRequestDTO(
        String description, Long customerId, List<Long> productsIds
) {
}
