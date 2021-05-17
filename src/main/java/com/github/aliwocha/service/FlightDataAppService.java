package com.github.aliwocha.service;

import com.github.aliwocha.exception.FlightNotFoundException;
import com.github.aliwocha.model.*;
import com.github.aliwocha.utils.JsonMapper;
import com.github.aliwocha.utils.WeightUnitConverter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FlightDataAppService {
    private static final String FLIGHT_ENTITY_JSON_FILE_PATH = "/json/flightEntity.json";
    private static final String CARGO_ENTITY_JSON_FILE_PATH = "/json/flightLoadEntity.json";

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    private final List<FlightEntity> flights = JsonMapper.mapJsonToFlightEntity(FLIGHT_ENTITY_JSON_FILE_PATH);
    private final List<FlightLoadEntity> flightsLoadList = JsonMapper.mapJsonToCargoEntity(CARGO_ENTITY_JSON_FILE_PATH);

    private final Scanner scanner = new Scanner(System.in);

    public void printResultsForRequestedFlight() {
        int flightNumber = getFlightNumberFromUser();
        Date departureDate = getDepartureDateFromUser();

        Weight cargoWeight = getCargoWeight(flightNumber, departureDate);
        Weight baggageWeight = getBaggageWeight(flightNumber, departureDate);

        double weightSumInPounds = cargoWeight.getPounds() + baggageWeight.getPounds();
        double weightSumInKilograms = cargoWeight.getKilograms() + baggageWeight.getKilograms();

        final DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);

        System.out.println("Total baggage weight for the flight number " + flightNumber + ": "
                + df.format(baggageWeight.getKilograms()) + "kg / " + df.format(baggageWeight.getPounds()) + "lb");
        System.out.println("Total cargo weight for flight number " + flightNumber + ": "
                + df.format(cargoWeight.getKilograms()) + "kg / " + df.format(cargoWeight.getPounds()) + "lb");
        System.out.println("Total load for the flight number " + flightNumber + ": " + df.format(weightSumInKilograms)
                + "kg / " + df.format(weightSumInPounds) + "lb");
    }

    public void printResultsForRequestedAirport() {
        System.out.println("Please provide IATA Airport Code:");
        String airportIATACode = scanner.nextLine();

        long numberOfFlightsDeparting = getNumberOfFlightsDeparting(airportIATACode);
        long numberOfFlightsArriving = getNumberOfFlightsArriving(airportIATACode);

        int numberOfBaggageDeparting = getNumberOfBaggageDeparting(airportIATACode);
        int numberOfBaggageArriving = getNumberOfBaggageArriving(airportIATACode);

        System.out.println("Total number of flights departing from the airport \"" + airportIATACode + "\" - "
                + numberOfFlightsDeparting);
        System.out.println("Total number of flights arriving to the airport \"" + airportIATACode + "\" - "
                + numberOfFlightsArriving);

        System.out.println("Total number of baggage departing from the airport \"" + airportIATACode + "\" - "
                + numberOfBaggageDeparting);
        System.out.println("Total number of baggage arriving from the airport \"" + airportIATACode + "\" - "
                + numberOfBaggageArriving);
    }

    private int getFlightNumberFromUser() {
        int flightNumber = 0;

        boolean error = true;
        do {
            System.out.println("Please provide Flight Number:");

            try {
                flightNumber = scanner.nextInt();
                error = false;
            } catch (InputMismatchException e) {
                System.err.println("Flight Number must be a number! Please try again.");
            } finally {
                scanner.nextLine();
            }

        } while (error);

        return flightNumber;
    }

    private Date getDepartureDateFromUser() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

        Date departureDate = new Date();
        boolean error = true;
        do {
            System.out.println("Please provide departure date of the flight using date format \"YYYY-MM-ddThh:mm:ss\""
                    + " with time offset (e.g. 2015-07-06T06:28:23-0200):");

            try {
                departureDate = formatter.parse(scanner.nextLine());
                error = false;
            } catch (ParseException e) {
                System.err.println("Wrong date format! Please try again.");
            }

        } while (error);

        return departureDate;
    }

    private Weight getCargoWeight(int flightNumber, Date departureDate) {
        FlightEntity flight = getFlightByFlightNumberAndDepartureDate(flightNumber, departureDate);
        FlightLoadEntity flightLoad = getFlightLoadByFlightId(flight.getFlightId());

        Cargo[] cargo = flightLoad.getCargo();
        double weightInPounds = sumWeightInPounds(cargo);
        double weightInKilograms = sumWeightInKilograms(cargo);

        return new Weight(weightInPounds, weightInKilograms);
    }

    private Weight getBaggageWeight(int flightNumber, Date departureDate) {
        FlightEntity flight = getFlightByFlightNumberAndDepartureDate(flightNumber, departureDate);
        FlightLoadEntity flightLoad = getFlightLoadByFlightId(flight.getFlightId());

        Baggage[] baggage = flightLoad.getBaggage();
        double weightInPounds = sumWeightInPounds(baggage);
        double weightInKilograms = sumWeightInKilograms(baggage);

        return new Weight(weightInPounds, weightInKilograms);
    }

    private FlightEntity getFlightByFlightNumberAndDepartureDate(int flightNumber, Date departureDate) {
        return flights.stream()
                .filter(f -> f.getFlightNumber() == flightNumber && f.getDepartureDate().equals(departureDate))
                .findFirst()
                .orElseThrow(() -> new FlightNotFoundException("Provided Flight Number or Departure Date not found."));
    }

    private FlightLoadEntity getFlightLoadByFlightId(int flightId) {
        return flightsLoadList.stream()
                .filter(fl -> fl.getFlightId() == flightId)
                .findFirst()
                .orElseThrow(() -> new FlightNotFoundException("Provided Flight Number not found."));
    }

    private double sumWeightInPounds(Freight[] freight) {
        double weightInPounds = 0;

        for (Freight f : freight) {
            if (f.getWeightUnit().equals("lb")) {
                weightInPounds += f.getWeight();
            } else if (f.getWeightUnit().equals("kg")) {
                weightInPounds += WeightUnitConverter.toPounds(f.getWeight());
            }
        }

        return weightInPounds;
    }

    private double sumWeightInKilograms(Freight[] freight) {
        double weightInKilograms = 0;

        for (Freight f : freight) {
            if (f.getWeightUnit().equals("kg")) {
                weightInKilograms += f.getWeight();
            } else if (f.getWeightUnit().equals("lb")) {
                weightInKilograms += WeightUnitConverter.toKilograms(f.getWeight());
            }
        }

        return weightInKilograms;
    }

    private long getNumberOfFlightsDeparting(String airportIATACode) {
        return flights.stream()
                .filter(f -> f.getDepartureAirportIATACode().equals(airportIATACode))
                .count();
    }

    private long getNumberOfFlightsArriving(String airportIATACode) {
        return flights.stream()
                .filter(f -> f.getArrivalAirportIATACode().equals(airportIATACode))
                .count();
    }

    private int getNumberOfBaggageDeparting(String airportIATACode) {
        List<FlightEntity> filteredFlights = flights.stream()
                .filter(f -> f.getDepartureAirportIATACode().equals(airportIATACode))
                .collect(Collectors.toList());

        return countBaggage(filteredFlights);
    }

    private int getNumberOfBaggageArriving(String airportIATACode) {
        List<FlightEntity> filteredFlights = flights.stream()
                .filter(f -> f.getArrivalAirportIATACode().equals(airportIATACode))
                .collect(Collectors.toList());

        return countBaggage(filteredFlights);
    }

    private int countBaggage(List<FlightEntity> filteredFlights) {
        int numberOfBaggage = 0;

        for (FlightEntity flight : filteredFlights) {
            FlightLoadEntity flightLoad = flightsLoadList.stream()
                    .filter(fl -> fl.getFlightId() == flight.getFlightId())
                    .findFirst()
                    .orElseThrow(() -> new FlightNotFoundException("Provided Flight Number is incorrect."));

            Baggage[] baggage = flightLoad.getBaggage();
            for (Baggage b : baggage) {
                numberOfBaggage += b.getPieces();
            }
        }

        return numberOfBaggage;
    }
}
