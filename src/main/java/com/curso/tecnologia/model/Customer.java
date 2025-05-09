package com.curso.tecnologia.model;


import java.util.List;
import java.util.Objects;

public class Customer {

    private String name;
    private String cpf;
    private List<Purchase> purchases;

    public Customer(String name, String cpf, List<Purchase> purchases) {
        this.name = name;
        this.cpf = cpf;
        this.purchases = purchases;
    }

    public Customer(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

    public Customer(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(name, customer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", purchases=" + purchases +
                '}';
    }

    public static final class Builder {
        private Customer customer;

        private Builder() {
            customer = new Customer();
        }

        public static Builder aCustomer() {
            return new Builder();
        }

        public Builder name(String name) {
            customer.setName(name);
            return this;
        }

        public Builder cpf(String cpf) {
            customer.setCpf(cpf);
            return this;
        }

        public Builder purchases(List<Purchase> purchases) {
            customer.setPurchases(purchases);
            return this;
        }

        public Customer build() {
            return customer;
        }
    }
}