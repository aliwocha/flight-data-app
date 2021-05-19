package com.github.aliwocha.model;

import java.util.Date;
import java.util.Objects;

public class FlightEntity {

    private Integer flightId;
    private Integer flightNumber;
    private String departureAirportIATACode;
    private String arrivalAirportIATACode;
    private Date departureDate;

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Integer getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(Integer flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirportIATACode() {
        return departureAirportIATACode;
    }

    public void setDepartureAirportIATACode(String departureAirportIATACode) {
        this.departureAirportIATACode = departureAirportIATACode;
    }

    public String getArrivalAirportIATACode() {
        return arrivalAirportIATACode;
    }

    public void setArrivalAirportIATACode(String arrivalAirportIATACode) {
        this.arrivalAirportIATACode = arrivalAirportIATACode;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightEntity that = (FlightEntity) o;
        return Objects.equals(flightId, that.flightId) && Objects.equals(flightNumber, that.flightNumber)
                && Objects.equals(departureAirportIATACode, that.departureAirportIATACode)
                && Objects.equals(arrivalAirportIATACode, that.arrivalAirportIATACode)
                && Objects.equals(departureDate, that.departureDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, flightNumber, departureAirportIATACode, arrivalAirportIATACode, departureDate);
    }
}
