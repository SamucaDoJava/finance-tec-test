package com.curso.tecnologia.service;

import com.curso.tecnologia.dto.AggregatedPurchaseResponseDTO;
import com.curso.tecnologia.dto.CustomerPurchaseDTO;
import com.curso.tecnologia.dto.ProductDTO;
import com.curso.tecnologia.dto.PurchaseDTO;
import com.curso.tecnologia.indicator.SortDirection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseAnalysisServiceTest  {

    @Mock
    private CustomerService customerService;
    @Mock
    private ProductService productService;
    @InjectMocks
    private PurchaseAnalysisService service;

    @Test
    public void should_get_all_purchased_by_value() {
        List<ProductDTO> listProducts = createProductList();

        when(productService.fetchProducts()).thenReturn(listProducts);
        when(customerService.fetchCustomerPurchases()).thenReturn(createCustomerList());

        List<AggregatedPurchaseResponseDTO> allPurchasesSortedByValue = service.getAllPurchasesSortedByValue(1, 11, SortDirection.ASC);
        assertEquals(2, allPurchasesSortedByValue.size());
        assertEquals(BigDecimal.valueOf(80), allPurchasesSortedByValue.getFirst().getTotalValue());
        assertEquals(BigDecimal.valueOf(150), allPurchasesSortedByValue.get(1).getTotalValue());
    }

    private List<ProductDTO> createProductList() {
        return List.of(createProduct(1, 2018, new BigDecimal("10")),
                createProduct(2, 2021, new BigDecimal("20")),
                createProduct(3, 1999, new BigDecimal("30")),
                createProduct(4, 2021, new BigDecimal("40000")));
    }

    private ProductDTO createProduct(int code, int purchasedYear, BigDecimal price) {
        return ProductDTO
                .builder()
                .code(code)
                .wineType("wine" + code)
                .vintage("vintage" + code)
                .purchaseYear(purchasedYear)
                .rawPrice(price)
                .build();
    }

    private List<CustomerPurchaseDTO> createCustomerList() {
        List<CustomerPurchaseDTO> customerPurchaseDTOList = new ArrayList<>();
        customerPurchaseDTOList.add(CustomerPurchaseDTO
                .builder()
                .name("Vitória Alícia Mendes")
                .cpf("20623850567")
                .purchases(List.of(createPurchased(1, 8)))
                .build());
        customerPurchaseDTOList.add(CustomerPurchaseDTO
                .builder()
                .name("Teresinha Daniela Galvão")
                .cpf("04372012950")
                .purchases(List.of(createPurchased(1, 3),
                        createPurchased(2, 3),
                        createPurchased(3, 2)))
                .build());

        return customerPurchaseDTOList;
    }

    private PurchaseDTO createPurchased(int code, int quantity) {
        return PurchaseDTO
                .builder()
                .code(code)
                .quantity(quantity)
                .build();
    }

}