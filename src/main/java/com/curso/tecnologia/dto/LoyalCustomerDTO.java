package com.curso.tecnologia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyalCustomerDTO {

    private String customerName;
    private String customerCpf;
    private double totalSpent;

}