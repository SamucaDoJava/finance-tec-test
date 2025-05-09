package com.curso.tecnologia.service;

import com.curso.tecnologia.builder.ProductMapper;
import com.curso.tecnologia.dto.ProductDTO;
import com.curso.tecnologia.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class ProductService {

    private final RestTemplate restTemplate;
    private final ProductMapper productMapper;

    public ProductService(RestTemplate restTemplate, ProductMapper productMapper) {
        this.restTemplate = restTemplate;
        this.productMapper = productMapper;
    }

    private static final String URL = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";

    public List<Product> fetchProducts() {
        try {
            String json = restTemplate.getForObject(URL, String.class);

            Type listType = new TypeToken<List<ProductDTO>>() {
            }.getType();

            List<ProductDTO> dtos = new Gson().fromJson(json, listType);
            return productMapper.toListEntity(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
