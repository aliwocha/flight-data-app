package com.github.aliwocha.model;

import java.util.Objects;

public class Weight {

    private double pounds;
    private double kilograms;

    public Weight(double pounds, double kilograms) {
        this.pounds = pounds;
        this.kilograms = kilograms;
    }

    public double getPounds() {
        return pounds;
    }

    public void setPounds(double pounds) {
        this.pounds = pounds;
    }

    public double getKilograms() {
        return kilograms;
    }

    public void setKilograms(double kilograms) {
        this.kilograms = kilograms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weight weight = (Weight) o;
        return Double.compare(weight.pounds, pounds) == 0 && Double.compare(weight.kilograms, kilograms) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pounds, kilograms);
    }
}
