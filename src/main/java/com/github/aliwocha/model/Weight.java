package com.github.aliwocha.model;

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
}
