package com.frostox.util.taxbaato.entities;

import java.util.UUID;

/**
 * Created by roger on 1/4/2017.
 */
public class Person {

    private String name;
    private Double amount;
    private Double tax;
    private String UUID;

    public Person(String name, Double amount, Double tax) {
        this.name = name;
        this.amount = amount;
        this.tax = tax;
        UUID = java.util.UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return UUID.equals(person.UUID);

    }

    @Override
    public int hashCode() {
        return UUID.hashCode();
    }
}
