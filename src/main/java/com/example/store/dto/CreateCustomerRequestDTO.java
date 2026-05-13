package com.example.store.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequestDTO (
        @NotBlank(message = "name must not be blank")
        String name
) {
}
