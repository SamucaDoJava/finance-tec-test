package com.curso.tecnologia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedPurchaseResponseDTO {

    private String customerName;
    private String customerCpf;
    private List<PurchasedItemDTO> items;
    private BigDecimal totalValue;

}