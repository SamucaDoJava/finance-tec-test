package com.curso.tecnologia.controller;

import com.curso.tecnologia.dto.CustomerPurchaseDTO;
import com.curso.tecnologia.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/purchases")
    public List<CustomerPurchaseDTO> getAllCustomerPurchases() {
        return customerService.fetchCustomerPurchases();
    }

    @GetMapping("/clear-redis-cache")
    public void clearCache(){
        customerService.clearCustomerPurchaseCache();
    }

}
