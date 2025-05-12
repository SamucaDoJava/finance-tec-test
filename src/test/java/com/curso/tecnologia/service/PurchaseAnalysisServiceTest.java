package com.curso.tecnologia.service;

import com.curso.tecnologia.dto.*;
import com.curso.tecnologia.exception.ResourceNotFoundException;
import com.curso.tecnologia.indicator.SortDirection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    public void should_get_all_purchased_by_value_order_asc() {
        List<ProductDTO> listProducts = createProductList();

        when(productService.fetchProducts()).thenReturn(listProducts);
        when(customerService.fetchCustomerPurchases()).thenReturn(createCustomerList());

        List<AggregatedPurchaseResponseDTO> allPurchasesSortedByValue = service.getAllPurchasesSortedByValue(1, 11, SortDirection.ASC);
        assertEquals(2, allPurchasesSortedByValue.size());
        assertEquals(BigDecimal.valueOf(80), allPurchasesSortedByValue.getFirst().getTotalValue());
        assertEquals(BigDecimal.valueOf(150), allPurchasesSortedByValue.get(1).getTotalValue());
    }

    @Test
    public void should_get_all_purchased_by_value_order_desc() {
        List<ProductDTO> listProducts = createProductList();

        when(productService.fetchProducts()).thenReturn(listProducts);
        when(customerService.fetchCustomerPurchases()).thenReturn(createCustomerList());

        List<AggregatedPurchaseResponseDTO> allPurchasesSortedByValue = service.getAllPurchasesSortedByValue(1, 11, SortDirection.DESC);
        assertEquals(2, allPurchasesSortedByValue.size());
        assertEquals(BigDecimal.valueOf(80), allPurchasesSortedByValue.get(1).getTotalValue());
        assertEquals(BigDecimal.valueOf(150), allPurchasesSortedByValue.getFirst().getTotalValue());
    }

    @Test
    public void should_get_biggest_purchased_by_year() {
        List<CustomerPurchaseDTO> customerPurchaseDTOList = createCustomerList();
        customerPurchaseDTOList.add(createCustomerPurchased("Geraldo Pedro Julio Nascimento", "05870189179",
                List.of(createPurchased(4, 3))));
        when(productService.fetchProducts()).thenReturn(createProductList());
        when(customerService.fetchCustomerPurchases()).thenReturn(customerPurchaseDTOList);
        PurchaseResponseDTO biggestPurchaseByYear = service.getBiggestPurchaseByYear(2021);
        assertEquals("05870189179", biggestPurchaseByYear.getCustomerCpf());
        assertEquals(new BigDecimal(120000), biggestPurchaseByYear.getTotalValue());
    }

    @Test
    public void should_throw_when_purchased_not_found() {
        when(productService.fetchProducts()).thenReturn(createProductList());
        when(customerService.fetchCustomerPurchases()).thenReturn(createCustomerList());
        assertThrows(ResourceNotFoundException.class, () -> service.getBiggestPurchaseByYear(1995));
    }

    @Test
    public void should_get_top_customers() {
        List<CustomerPurchaseDTO> customerPurchaseDTOList = createCustomerList();
        customerPurchaseDTOList.add(createCustomerPurchased("Geraldo Pedro Julio Nascimento", "05870189179",
                List.of(createPurchased(4, 3))));
        customerPurchaseDTOList.add(createCustomerPurchased("Andreia Emanuelly da Mata", "27737287426",
                List.of(createPurchased(2, 1))));

        when(productService.fetchProducts()).thenReturn(createProductList());
        when(customerService.fetchCustomerPurchases()).thenReturn(customerPurchaseDTOList);
        List<LoyalCustomerDTO> top3LoyalCustomers = service.getTop3LoyalCustomers();
        assertEquals(3, top3LoyalCustomers.size());
        assertEquals("05870189179", top3LoyalCustomers.getFirst().getCustomerCpf());
        assertFalse(top3LoyalCustomers.stream().anyMatch(c -> c.getCustomerCpf().equals("27737287426")));
    }

    @Test
    public void should_get_wine_recommendations() {
        List<CustomerPurchaseDTO> customerPurchaseDTOList = createCustomerList();
        customerPurchaseDTOList.add(createCustomerPurchased("Geraldo Pedro Julio Nascimento", "05870189179",
                List.of(createPurchased(4, 3),
                        createPurchased(1, 2),
                        createPurchased(3, 1),
                        createPurchased(4, 2))
            )
        );

        when(productService.fetchProducts()).thenReturn(createProductList());
        when(customerService.fetchCustomerPurchases()).thenReturn(customerPurchaseDTOList);

        assertEquals("wine4", service.getMostPurchasedWineTypeByCustomerCpf("05870189179"));
    }

    @Test
    public void should_not_get_wine_recommendations() {
        List<CustomerPurchaseDTO> customerPurchaseDTOList = createCustomerList();
        customerPurchaseDTOList.add(createCustomerPurchased("Geraldo Pedro Julio Nascimento", "05870189179", List.of()));

        when(productService.fetchProducts()).thenReturn(createProductList());
        when(customerService.fetchCustomerPurchases()).thenReturn(customerPurchaseDTOList);

        assertEquals("Sem recomendação", service.getMostPurchasedWineTypeByCustomerCpf("05870189179"));
    }

    @Test
    public void should_throw_exception_when_no_cpf() {
        when(customerService.fetchCustomerPurchases()).thenReturn(createCustomerList());

        assertThrows(ResourceNotFoundException.class, () -> service.getMostPurchasedWineTypeByCustomerCpf("05870189179"));
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
        customerPurchaseDTOList.add(
                createCustomerPurchased("Vitória Alícia Mendes", "20623850567",
                        List.of(createPurchased(1, 8)))
        );

        customerPurchaseDTOList.add(
                createCustomerPurchased("Teresinha Daniela Galvão", "04372012950",
                        List.of(createPurchased(1, 3),
                            createPurchased(2, 3),
                            createPurchased(3, 2)
                        )
                )
        );

        return customerPurchaseDTOList;
    }

    private CustomerPurchaseDTO createCustomerPurchased(String name, String cpf, List<PurchaseDTO> purchaseDTOList) {
        return CustomerPurchaseDTO
                .builder()
                .name(name)
                .cpf(cpf)
                .purchases(purchaseDTOList)
                .build();
    }

    private PurchaseDTO createPurchased(int code, int quantity) {
        return PurchaseDTO
                .builder()
                .code(code)
                .quantity(quantity)
                .build();
    }

}