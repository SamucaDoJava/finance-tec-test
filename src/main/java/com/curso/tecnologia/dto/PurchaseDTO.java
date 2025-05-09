package com.curso.tecnologia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PurchaseDTO {

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("quantidade")
    private int quantity;
}