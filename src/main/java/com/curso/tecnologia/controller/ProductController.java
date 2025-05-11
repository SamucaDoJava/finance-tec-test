package com.curso.tecnologia.controller;

import com.curso.tecnologia.dto.ProductDTO;
import com.curso.tecnologia.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @GetMapping("all-products")
    public List<ProductDTO> getAllProducts() {
        return productService.fetchProducts();
    }

    @GetMapping("/clear-redis-cache")
    public void clearCache(){
        productService.clearProductsCache();
    }


}
