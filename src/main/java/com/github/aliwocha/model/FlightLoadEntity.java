package com.github.aliwocha.model;

import java.util.Arrays;
import java.util.Objects;

public class FlightLoadEntity {

    private Integer flightId;
    private Baggage[] baggage;
    private Cargo[] cargo;

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightLoadEntity that = (FlightLoadEntity) o;
        return Objects.equals(flightId, that.flightId) && Arrays.equals(baggage, that.baggage)
                && Arrays.equals(cargo, that.cargo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(flightId);
        result = 31 * result + Arrays.hashCode(baggage);
        result = 31 * result + Arrays.hashCode(cargo);
        return result;
    }
}
