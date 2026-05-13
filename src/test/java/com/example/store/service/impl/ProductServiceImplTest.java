package com.example.store.service.impl;

import com.example.store.dto.CreateProductRequestDTO;
import com.example.store.dto.PageResponse;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.exceptions.EntityNotFoundException;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProduct_mapsAndSavesProduct() {
        CreateProductRequestDTO request = new CreateProductRequestDTO("Laptop");
        Product toSave = new Product();
        Product saved = new Product();
        ProductDTO response = new ProductDTO(1L, "Laptop", List.of());

        when(productMapper.createProductRequestDTOToProductDTO(request)).thenReturn(toSave);
        when(productRepository.save(toSave)).thenReturn(saved);
        when(productMapper.productToProductDTO(saved)).thenReturn(response);

        ProductDTO result = productService.createProduct(request);

        assertSame(response, result);
        verify(productMapper).createProductRequestDTOToProductDTO(request);
        verify(productRepository).save(toSave);
        verify(productMapper).productToProductDTO(saved);
    }

    @Test
    void testGetAllProducts_returnsMappedPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Long> productIds = new PageImpl<>(List.of(1L, 2L), pageable, 2L);
        Product product1 = new Product();
        Product product2 = new Product();
        ProductDTO dto1 = new ProductDTO(1L, "P1", List.of());
        ProductDTO dto2 = new ProductDTO(2L, "P2", List.of());

        when(productRepository.findProductIds(pageable)).thenReturn(productIds);
        when(productRepository.findAllWithOrdersByIds(List.of(1L, 2L))).thenReturn(List.of(product1, product2));
        when(productMapper.productToProductDTO(product1)).thenReturn(dto1);
        when(productMapper.productToProductDTO(product2)).thenReturn(dto2);

        PageResponse<ProductDTO> result = productService.getAllProducts(pageable);

        assertEquals(2, result.content().size());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(2L, result.totalElements());
    }

    @Test
    void testFindProductById_returnsProduct() {
        Product product = new Product();
        ProductDTO response = new ProductDTO(1L, "Laptop", List.of());

        when(productRepository.findByIdWithOrders(1L)).thenReturn(Optional.of(product));
        when(productMapper.productToProductDTO(product)).thenReturn(response);

        ProductDTO result = productService.findProductById(1L);

        assertSame(response, result);
        verify(productRepository).findByIdWithOrders(1L);
    }

    @Test
    void testFindProductById_whenNotFound_throwsException() {
        when(productRepository.findByIdWithOrders(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.findProductById(99L));
        verify(productRepository).findByIdWithOrders(99L);
    }
}