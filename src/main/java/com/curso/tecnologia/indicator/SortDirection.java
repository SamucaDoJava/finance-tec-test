package com.curso.tecnologia.indicator;

public enum SortDirection {
    ASC, DESC;

    public static SortDirection from(String value) {
        try {
            return SortDirection.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Direção de ordenação inválida: " + value + ". Use ASC ou DESC.");
        }
    }
}
