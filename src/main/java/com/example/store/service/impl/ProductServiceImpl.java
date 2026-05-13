package com.example.store.service.impl;

import com.example.store.dto.CreateProductRequestDTO;
import com.example.store.dto.PageResponse;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.exceptions.EntityNotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import com.example.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDTO createProduct(CreateProductRequestDTO createProductRequestDTO) {
        Product product = productMapper.createProductRequestDTOToProductDTO(createProductRequestDTO);
        return productMapper.productToProductDTO(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Long> productIds = productRepository.findProductIds(pageable);

        List<Product> products = productRepository.findAllWithOrdersByIds(productIds.getContent());

        List<ProductDTO> productDTOs = products
                .stream().map(productMapper::productToProductDTO)
                .toList();
        return new PageResponse<>(productDTOs,pageable.getPageNumber(), pageable.getPageSize(), productIds.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findProductById(Long id) {
        Product product = productRepository.findByIdWithOrders(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Product with id %d not found", id)));
        return productMapper.productToProductDTO(product);
    }
}
