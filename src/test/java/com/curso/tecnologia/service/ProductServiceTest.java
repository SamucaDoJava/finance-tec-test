package com.curso.tecnologia.service;

import com.curso.tecnologia.builder.ProductMapper;
import com.curso.tecnologia.dto.ProductDTO;
import com.curso.tecnologia.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        // Força o valor da variável injetada por @Value
        ReflectionTestUtils.setField(productService, "productServiceUrl", "http://fake-url.com/api/products");
    }

    @Test
    void shouldFetchProductsAndMapToEntities() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        List<ProductDTO> dtoList = mapper.readValue(MOCK_JSON, new TypeReference<>() {});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(MOCK_JSON);
        when(productMapper.toListEntity(dtoList)).thenReturn(List.of(
                new Product(1, "Tinto", 229.99, "2017", 2018),
                new Product(2, "Branco", 126.50, "2018", 2019)
        ));

        // Act
        List<ProductDTO> result = productService.fetchProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tinto", result.get(0).getWineType());
        assertEquals("Branco", result.get(1).getWineType());

        // Verify interactions
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
        verify(productMapper, times(1)).toListEntity(dtoList);
    }

    private static final String MOCK_JSON = """
        [
            {
                "codigo": 1,
                "tipo_vinho": "Tinto",
                "preco": "229.99",
                "safra": "2017",
                "ano_compra": 2018
            },
            {
                "codigo": 2,
                "tipo_vinho": "Branco",
                "preco": "126.50JS:126.5",
                "safra": "2018",
                "ano_compra": 2019
            }
        ]
    """;
}
