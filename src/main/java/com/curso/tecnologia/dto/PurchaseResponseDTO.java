package com.curso.tecnologia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDTO {

    private String customerName;
    private String customerCpf;
    private ProductDTO product;
    private int quantity;
    private BigDecimal totalValue;

}
