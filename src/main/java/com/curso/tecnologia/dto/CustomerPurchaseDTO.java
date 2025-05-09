package com.curso.tecnologia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CustomerPurchaseDTO {

    @JsonProperty("nome")
    private String name;

    @JsonProperty("cpf")
    private String cpf;

    @JsonProperty("compras")
    private List<PurchaseDTO> purchases;
}