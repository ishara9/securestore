package com.example.store.mapper;

import com.example.store.dto.CreateProductRequestDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product createProductRequestDTOToProductDTO(CreateProductRequestDTO createProductRequestDTO);

    ProductDTO productToProductDTO(Product product);

    default Page<ProductDTO> productsToProductDTOs(Page<Product> products){
        return products.map(this::productToProductDTO);
    }
}
