package com.curso.tecnologia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDTO {

    private String customerName;
    private String customerCpf;
    private ProductDTO product;
    private int quantity;
    private double totalValue;

}
