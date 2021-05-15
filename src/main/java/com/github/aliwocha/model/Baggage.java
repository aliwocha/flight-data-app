package com.github.aliwocha.model;

public class Baggage extends FlightLoad {

    public Baggage(int id, int weight, String weightUnit, int pieces) {
        super(id, weight, weightUnit, pieces);
    }

    public Baggage() {
    }
}
