package com.example.store.mapper;

import com.example.store.dto.CreateProductRequestDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends PageMapper<Product, ProductDTO> {
    Product createProductRequestDTOToProductDTO(CreateProductRequestDTO createProductRequestDTO);

    ProductDTO productToProductDTO(Product product);
}
