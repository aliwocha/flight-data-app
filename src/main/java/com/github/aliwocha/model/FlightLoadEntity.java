package com.github.aliwocha.model;

public class FlightLoadEntity {

    private int flightId;
    private Baggage[] baggage;
    private Cargo[] cargo;

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
