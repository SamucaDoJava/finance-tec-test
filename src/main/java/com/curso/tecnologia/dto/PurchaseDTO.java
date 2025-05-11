package com.curso.tecnologia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class PurchaseDTO implements Serializable {

    @JsonProperty("codigo")
    private int code;

    @JsonProperty("quantidade")
    private int quantity;
}