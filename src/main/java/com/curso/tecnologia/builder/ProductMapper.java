package com.curso.tecnologia.builder;

import com.curso.tecnologia.dto.ProductDTO;
import com.curso.tecnologia.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Product toEntity(ProductDTO dto) {
        return modelMapper.map(dto, Product.class);
    }

    public ProductDTO toDTO(Product entity) {
        return modelMapper.map(entity, ProductDTO.class);
    }

    public List<Product> toListEntity(List<ProductDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<ProductDTO> toListDTO(List<Product> entityList) {
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private double parsePrice(String rawPrice) {
        if (rawPrice == null || rawPrice.isBlank()) {
            return 0.0;
        }
        String cleanedPrice = rawPrice.replaceAll("JS:.*", "");
        return Double.parseDouble(cleanedPrice);
    }
}
