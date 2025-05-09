package com.curso.tecnologia.model;

import java.util.Objects;

public class Product {
    private int code;
    private String wineType;
    private double price;
    private String vintage;
    private int purchaseYear;

    public Product() {
    }

    public Product(int code, String wineType, double price, String vintage, int purchaseYear) {
        this.code = code;
        this.wineType = wineType;
        this.price = price;
        this.vintage = vintage;
        this.purchaseYear = purchaseYear;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getWineType() {
        return wineType;
    }

    public void setWineType(String wineType) {
        this.wineType = wineType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public int getPurchaseYear() {
        return purchaseYear;
    }

    public void setPurchaseYear(int purchaseYear) {
        this.purchaseYear = purchaseYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return code == product.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public static final class Builder {
        private Product product;

        private Builder() {
            product = new Product();
        }

        public static Builder aProduct() {
            return new Builder();
        }

        public Builder code(int code) {
            product.setCode(code);
            return this;
        }

        public Builder wineType(String wineType) {
            product.setWineType(wineType);
            return this;
        }

        public Builder price(double price) {
            product.setPrice(price);
            return this;
        }

        public Builder vintage(String vintage) {
            product.setVintage(vintage);
            return this;
        }

        public Builder purchaseYear(int purchaseYear) {
            product.setPurchaseYear(purchaseYear);
            return this;
        }

        public Product build() {
            return product;
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "code=" + code +
                ", wineType='" + wineType + '\'' +
                ", price=" + price +
                ", vintage='" + vintage + '\'' +
                ", purchaseYear=" + purchaseYear +
                '}';
    }
}

