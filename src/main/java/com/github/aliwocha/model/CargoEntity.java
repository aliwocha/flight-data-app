package com.github.aliwocha.model;

public class CargoEntity {

    private int flightId;
    private Baggage[] baggage;
    private Cargo[] cargo;

    public CargoEntity(int flightId, Baggage[] baggage, Cargo[] cargo) {
        this.flightId = flightId;
        this.baggage = baggage;
        this.cargo = cargo;
    }

    public CargoEntity() {
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public Baggage[] getBaggage() {
        return baggage;
    }

    public void setBaggage(Baggage[] baggage) {
        this.baggage = baggage;
    }

    public Cargo[] getCargo() {
        return cargo;
    }

    public void setCargo(Cargo[] cargo) {
        this.cargo = cargo;
    }
}
