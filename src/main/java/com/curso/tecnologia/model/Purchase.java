package com.curso.tecnologia.model;

public class Purchase {
    private String code;
    private int quantity;

    public Purchase(){

    }

    public Purchase(String code, int quantity) {
        this.code = code;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    @Override
    public String toString() {
        return "Purchase{" +
                "code='" + code + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public static final class Builder {
        private Purchase purchase;

        private Builder() {
            purchase = new Purchase();
        }

        public static Builder aPurchase() {
            return new Builder();
        }

        public Builder code(String code) {
            purchase.setCode(code);
            return this;
        }

        public Builder quantity(int quantity) {
            purchase.setQuantity(quantity);
            return this;
        }

        public Purchase build() {
            return purchase;
        }
    }
}
