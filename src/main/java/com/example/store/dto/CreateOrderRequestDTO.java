package com.example.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequestDTO(
        @NotBlank(message = "description must not be blank")
        String description,
        @NotNull(message = "customerId must not be null")
        Long customerId,
        @NotEmpty(message = "products must not be empty")
        List<Long> productIds
) {
}
