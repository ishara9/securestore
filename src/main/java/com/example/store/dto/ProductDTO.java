package com.example.store.dto;

import java.util.List;

public record ProductDTO(Long id, String description, List<Long> orderIds) {

    public ProductDTO {
        if (orderIds == null) {
            orderIds = List.of();
        }
    }

}
