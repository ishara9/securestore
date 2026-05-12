package com.example.store.controller;

import com.example.store.dto.CreateProductRequestDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.exceptions.ItemNotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @PostMapping
    public ProductDTO createProduct(@RequestBody CreateProductRequestDTO createProductRequestDTO) {
        Product product = productMapper.createProductRequestDTOToProductDTO(createProductRequestDTO);
        return productMapper.productToProductDTO(productRepository.save(product));
    }

    @GetMapping
    public Page<ProductDTO> getAllProducts(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return productRepository.findAll(pageable).map(p -> new ProductDTO(
                p.getId(),
                p.getDescription(),
                p.getOrders().stream()
                        .map(Order::getId)
                        .toList()
        ));
    }

    @GetMapping("/{id}")
    public ProductDTO findProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException(String.format("Product with id %d not found", id)));
        return new ProductDTO(product.getId(), product.getDescription(), product.getOrders().stream().map(Order::getId).toList());
    }

}
