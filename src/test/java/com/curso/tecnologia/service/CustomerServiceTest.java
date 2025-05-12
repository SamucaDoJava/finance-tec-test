package com.curso.tecnologia.service;

import com.curso.tecnologia.dto.CustomerPurchaseDTO;
import com.curso.tecnologia.dto.ProductDTO;
import com.curso.tecnologia.exception.CustomerServiceException;
import com.curso.tecnologia.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(customerService, "customerAndPurchaseUrl", "http://fakeurl.com.br");
    }

    @Test
    public void should_fetch_customer_purchases() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<CustomerPurchaseDTO> dtoList = mapper.readValue(MOCK_JSON, new TypeReference<>() {});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(MOCK_JSON);

        List<CustomerPurchaseDTO> customerPurchaseDTOList = customerService.fetchCustomerPurchases();
        assertNotNull(customerPurchaseDTOList);
        assertEquals(dtoList, customerPurchaseDTOList);
    }

    @Test
    public void should_throw_exception_when_fetch_customer() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(RuntimeException.class);
        assertThrows(CustomerServiceException.class, () -> customerService.fetchCustomerPurchases());
    }

    private static final String MOCK_JSON = """
        [
            {
                "nome": "Geraldo Pedro Julio Nascimento",
                "cpf": "05870189179",
                "compras":[
                        {
                            "codigo": "1",
                            "quantidade": 6
                        },
                        {
                            "codigo": "15",
                            "quantidade": 4
                        },
                        {
                            "codigo": "10",
                            "quantidade": 2
                        },
                        {
                            "codigo": "5",
                            "quantidade": 3
                        },
                        {
                            "codigo": "2",
                            "quantidade": 5
                        }
                ]
            }
        ]
    """;

}