package com.curso.tecnologia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PurchaseDTO implements Serializable {

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("quantidade")
    private int quantity;
}