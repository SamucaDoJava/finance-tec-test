package com.curso.tecnologia.service;

import com.curso.tecnologia.dto.CustomerPurchaseDTO;
import com.curso.tecnologia.exception.CustomerServiceException;
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
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final RestTemplate restTemplate;
    @Value("${customer.and.purchase.service.url}")
    private String customerAndPurchaseUrl;

    public CustomerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("customerPurchases")
    public List<CustomerPurchaseDTO> fetchCustomerPurchases() {
        try {
            LOGGER.info(">>> [CACHE MISS] Chamando API externa customerAndPurchase");
            String json = restTemplate.getForObject(customerAndPurchaseUrl, String.class);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            LOGGER.error("Erro ao recuperar os dados da aplicação na requisição fetchCustomerPurchases, url: [{}]", customerAndPurchaseUrl, e);
            throw new CustomerServiceException("Erro ao recuperar os dados de clientes e compras.", e);
        }
    }

    @CacheEvict(value = "customerPurchases", allEntries = true)
    public void clearCustomerPurchaseCache() {
        LOGGER.info("Cache de customerPurchases limpo manualmente.");
    }

}
