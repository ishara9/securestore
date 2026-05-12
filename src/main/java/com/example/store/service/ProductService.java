package com.example.store.service;

import com.example.store.dto.CreateProductRequestDTO;
import com.example.store.dto.PageResponse;
import com.example.store.dto.ProductDTO;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDTO createProduct(CreateProductRequestDTO createProductRequestDTO);

    PageResponse<ProductDTO> getAllProducts(Pageable pageable);

    ProductDTO findProductById(Long id);
}
