package com.example.store.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProductRequestDTO(
        @NotBlank(message = "product description must not be blank")
        String description
) {
}
