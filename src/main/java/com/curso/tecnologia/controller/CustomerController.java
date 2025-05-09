package com.curso.tecnologia.controller;

import com.curso.tecnologia.dto.CustomerPurchaseDTO;
import com.curso.tecnologia.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/purchases")
    public List<CustomerPurchaseDTO> getAllCustomerPurchases() {
        return customerService.fetchCustomerPurchases();
    }
}
