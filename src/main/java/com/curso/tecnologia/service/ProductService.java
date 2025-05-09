package com.curso.tecnologia.service;

import com.curso.tecnologia.builder.ProductMapper;
import com.curso.tecnologia.dto.ProductDTO;
import com.curso.tecnologia.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final RestTemplate restTemplate;
    private final ProductMapper productMapper;

    public ProductService(RestTemplate restTemplate, ProductMapper productMapper) {
        this.restTemplate = restTemplate;
        this.productMapper = productMapper;
    }

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Cacheable("products")
    public List<ProductDTO> fetchProducts() {
        try {
            String json = restTemplate.getForObject(productServiceUrl, String.class);
            ObjectMapper mapper = new ObjectMapper();
            List<ProductDTO> productDTOs = mapper.readValue(json, new TypeReference<>() {});

            List<Product> products = productMapper.toListEntity(productDTOs);

            return productDTOs;
        } catch (Exception e) {
            LOGGER.error("Erro ao recuperar os dados da aplicação na requisição fetchProducts, url: [{}], Ex:[{}]", productServiceUrl, e);
            return List.of();
        }
    }

    @CacheEvict(value = "products", allEntries = true)
    public void clearProductsCache() {
        LOGGER.info("Cache de customerPurchases limpo manualmente.");
    }


}
