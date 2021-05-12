package com.github.aliwocha.model;

import java.util.List;

public class CargoEntity {

    private int flightId;
    private List<Baggage> baggageList;
    private List<Cargo> cargoList;

    public CargoEntity(int flightId, List<Baggage> baggageList, List<Cargo> cargoList) {
        this.flightId = flightId;
        this.baggageList = baggageList;
        this.cargoList = cargoList;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public List<Baggage> getBaggageList() {
        return baggageList;
    }

    public void setBaggageList(List<Baggage> baggageList) {
        this.baggageList = baggageList;
    }

    public List<Cargo> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<Cargo> cargoList) {
        this.cargoList = cargoList;
    }
}
