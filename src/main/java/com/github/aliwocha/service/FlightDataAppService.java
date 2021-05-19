package com.github.aliwocha.service;

import com.github.aliwocha.exception.AirportCodeNotFoundException;
import com.github.aliwocha.exception.FlightLoadNotFoundException;
import com.github.aliwocha.exception.FlightNotFoundException;
import com.github.aliwocha.model.*;
import com.github.aliwocha.utils.JsonMapper;
import com.github.aliwocha.utils.WeightUnitConverter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class FlightDataAppService {
    private static final String FLIGHT_ENTITY_JSON_FILE_PATH = "/json/flightEntity.json";
    private static final String FLIGHT_LOAD_ENTITY_JSON_FILE_PATH = "/json/flightLoadEntity.json";

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    private final List<FlightEntity> flights;
    private final List<FlightLoadEntity> flightsLoadList;

    private final Scanner scanner;

    public FlightDataAppService() {
        flights = JsonMapper.mapJsonToFlightEntity(FLIGHT_ENTITY_JSON_FILE_PATH);
        flightsLoadList = JsonMapper.mapJsonToFlightLoadEntity(FLIGHT_LOAD_ENTITY_JSON_FILE_PATH);
        scanner = new Scanner(System.in);
    }

    public void executeOption1() {
        try {
            Integer flightNumber = getFlightNumberFromUser();
            Date departureDate = getDepartureDateFromUser();

            printResultsForRequestedFlight(flightNumber, departureDate);
        } catch (FlightNotFoundException | FlightLoadNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void executeOption2() {
        try {
            String airportIATACode = getIATACodeFromUserIgnoreCase();
            printResultsForRequestedAirport(airportIATACode);
        } catch (FlightLoadNotFoundException | AirportCodeNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private void printResultsForRequestedFlight(final Integer flightNumber, final Date departureDate) {
        Weight baggageWeight = getBaggageWeight(flightNumber, departureDate);
        Weight cargoWeight = getCargoWeight(flightNumber, departureDate);

        double weightSumInPounds = baggageWeight.getPounds() + cargoWeight.getPounds();
        double weightSumInKilograms = baggageWeight.getKilograms() + cargoWeight.getKilograms();

        final DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);

        System.out.println("Total baggage weight for flight number " + flightNumber + ": "
                + df.format(baggageWeight.getKilograms()) + "kg / " + df.format(baggageWeight.getPounds()) + "lb");
        System.out.println("Total cargo weight for flight number " + flightNumber + ": "
                + df.format(cargoWeight.getKilograms()) + "kg / " + df.format(cargoWeight.getPounds()) + "lb");
        System.out.println("Total load for flight number " + flightNumber + ": " + df.format(weightSumInKilograms)
                + "kg / " + df.format(weightSumInPounds) + "lb");
    }

    private void printResultsForRequestedAirport(final String airportIATACode) {
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
        System.out.println("Total number of baggage arriving to the airport \"" + airportIATACode + "\" - "
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

    private String getIATACodeFromUserIgnoreCase() {
        System.out.println("Please provide IATA Airport Code:");
        String airportIATACode = scanner.nextLine().toUpperCase();

        Optional<FlightEntity> flight = flights.stream()
                .filter(f -> f.getDepartureAirportIATACode().equals(airportIATACode) || f.getArrivalAirportIATACode().equals(airportIATACode))
                .findAny();

        if (flight.isPresent()) {
            return airportIATACode;
        } else {
            throw new AirportCodeNotFoundException("Provided IATA Airport Code not found! Please try again.");
        }
    }

    private Weight getCargoWeight(final Integer flightNumber, final Date departureDate) {
        FlightEntity flight = getFlightByFlightNumberAndDepartureDate(flightNumber, departureDate);
        FlightLoadEntity flightLoad = getFlightLoadByFlightId(flight.getFlightId());

        Cargo[] cargo = flightLoad.getCargo();
        double weightInPounds = sumWeightInPounds(cargo);
        double weightInKilograms = sumWeightInKilograms(cargo);

        return new Weight(weightInPounds, weightInKilograms);
    }

    private Weight getBaggageWeight(final Integer flightNumber, final Date departureDate) {
        FlightEntity flight = getFlightByFlightNumberAndDepartureDate(flightNumber, departureDate);
        FlightLoadEntity flightLoad = getFlightLoadByFlightId(flight.getFlightId());

        Baggage[] baggage = flightLoad.getBaggage();
        double weightInPounds = sumWeightInPounds(baggage);
        double weightInKilograms = sumWeightInKilograms(baggage);

        return new Weight(weightInPounds, weightInKilograms);
    }

    private FlightEntity getFlightByFlightNumberAndDepartureDate(final Integer flightNumber, final Date departureDate) {
        return flights.stream()
                .filter(f -> f.getFlightNumber().equals(flightNumber) && f.getDepartureDate().equals(departureDate))
                .findFirst()
                .orElseThrow(() -> new FlightNotFoundException("Flight not found! Please try again."));
    }

    private FlightLoadEntity getFlightLoadByFlightId(final Integer flightId) {
        return flightsLoadList.stream()
                .filter(fl -> fl.getFlightId().equals(flightId))
                .findFirst()
                .orElseThrow(() -> new FlightLoadNotFoundException("Flight load for given flight not found! Please try again."));
    }

    private double sumWeightInPounds(final Freight[] freight) {
        double weightInPounds = 0;

        for (Freight f : freight) {
            if (f.getWeightUnit().equals("lb") && f.getWeight() > 0) {
                weightInPounds += f.getWeight();
            } else if (f.getWeightUnit().equals("kg")) {
                weightInPounds += WeightUnitConverter.toPounds(f.getWeight());
            }
        }

        return weightInPounds;
    }

    private double sumWeightInKilograms(final Freight[] freight) {
        double weightInKilograms = 0;

        for (Freight f : freight) {
            if (f.getWeightUnit().equals("kg") && f.getWeight() > 0) {
                weightInKilograms += f.getWeight();
            } else if (f.getWeightUnit().equals("lb")) {
                weightInKilograms += WeightUnitConverter.toKilograms(f.getWeight());
            }
        }

        return weightInKilograms;
    }

    private long getNumberOfFlightsDeparting(final String airportIATACode) {
        return flights.stream()
                .filter(f -> f.getDepartureAirportIATACode().equals(airportIATACode))
                .count();
    }

    private long getNumberOfFlightsArriving(final String airportIATACode) {
        return flights.stream()
                .filter(f -> f.getArrivalAirportIATACode().equals(airportIATACode))
                .count();
    }

    private int getNumberOfBaggageDeparting(final String airportIATACode) {
        List<FlightEntity> filteredFlights = flights.stream()
                .filter(f -> f.getDepartureAirportIATACode().equals(airportIATACode))
                .collect(Collectors.toList());

        return countBaggage(filteredFlights);
    }

    private int getNumberOfBaggageArriving(final String airportIATACode) {
        List<FlightEntity> filteredFlights = flights.stream()
                .filter(f -> f.getArrivalAirportIATACode().equals(airportIATACode))
                .collect(Collectors.toList());

        return countBaggage(filteredFlights);
    }

    private int countBaggage(final List<FlightEntity> filteredFlights) {
        int numberOfBaggage = 0;

        for (FlightEntity flight : filteredFlights) {
            FlightLoadEntity flightLoad = flightsLoadList.stream()
                    .filter(fl -> fl.getFlightId().equals(flight.getFlightId()))
                    .findFirst()
                    .orElseThrow(() -> new FlightLoadNotFoundException("Flight load for given flight not found! Please try again."));

            Baggage[] baggage = flightLoad.getBaggage();
            for (Baggage b : baggage) {
                if (b.getPieces() > 0) {
                    numberOfBaggage += b.getPieces();
                }
            }
        }

        return numberOfBaggage;
    }
}
