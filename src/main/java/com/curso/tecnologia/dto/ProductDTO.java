package com.curso.tecnologia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductDTO {

    @JsonProperty("codigo")
    private int code;

    @JsonProperty("tipo_vinho")
    private String wineType;

    @JsonProperty("preco")
    private String rawPrice;

    @JsonProperty("safra")
    private String vintage;

    @JsonProperty("ano_compra")
    private int purchaseYear;

}
