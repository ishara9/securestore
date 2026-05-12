package com.example.store.service.impl;

import com.example.store.dto.CreateProductRequestDTO;
import com.example.store.dto.PageResponse;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.exceptions.ItemNotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import com.example.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(CreateProductRequestDTO createProductRequestDTO) {
        Product product = productMapper.createProductRequestDTOToProductDTO(createProductRequestDTO);
        return productMapper.productToProductDTO(productRepository.save(product));
    }

    @Override
    public PageResponse<ProductDTO> getAllProducts(Pageable pageable) {
        return productMapper.mapPage(productRepository.findAll(pageable), p -> new ProductDTO(
                p.getId(),
                p.getDescription(),
                p.getOrders().stream()
                        .map(Order::getId)
                        .toList()
        ));
    }

    @Override
    public ProductDTO findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException(String.format("Product with id %d not found", id)));
        return new ProductDTO(product.getId(), product.getDescription(), product.getOrders().stream().map(Order::getId).toList());
    }
}
