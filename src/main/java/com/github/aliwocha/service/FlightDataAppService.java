package com.github.aliwocha.service;

import com.github.aliwocha.exception.NoSuchFlightException;
import com.github.aliwocha.exception.WrongDateFormatException;
import com.github.aliwocha.model.*;
import com.github.aliwocha.utils.JsonMapper;
import com.github.aliwocha.utils.WeightUnitConverter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FlightDataAppService {
    private static final String FLIGHT_ENTITY_JSON_FILE_PATH = "/json/flightEntity.json";
    private static final String CARGO_ENTITY_JSON_FILE_PATH = "/json/flightLoadEntity.json";

    private final Scanner scanner = new Scanner(System.in);

    public void printBaggageAndCargoWeightForRequestedFlight() {
        System.out.println("Please provide Flight Number:");
        int flightNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Please provide departure date of the flight using date format \"YYYY-MM-ddThh:mm:ssZ\":");
        final String datePattern = "yyyy-MM-dd'T'HH:mm:ssZ";

        SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
        Date departureDate;

        try {
            departureDate = formatter.parse(scanner.nextLine());
        } catch (ParseException e) {
            throw new WrongDateFormatException("Wrong date format!");
        }

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

    public void executeOption2() {
        System.out.println("Option 2");
    }

    private Weight getCargoWeight(int flightNumber, Date departureDate) {
        List<FlightEntity> flights = JsonMapper.mapJsonToFlightEntity(FLIGHT_ENTITY_JSON_FILE_PATH);
        List<FlightLoadEntity> flightsLoadList = JsonMapper.mapJsonToCargoEntity(CARGO_ENTITY_JSON_FILE_PATH);

        FlightEntity flight = flights.stream()
                .filter(f -> f.getFlightNumber() == flightNumber && f.getDepartureDate().equals(departureDate))
                .findFirst()
                .orElseThrow(() -> new NoSuchFlightException("Provided Flight Number or Departure Date is incorrect."));

        FlightLoadEntity flightLoad = flightsLoadList.stream()
                .filter(fl -> fl.getFlightId() == flight.getFlightId())
                .findFirst()
                .orElseThrow(() -> new NoSuchFlightException("Provided Flight Number is incorrect."));

        Cargo[] cargo = flightLoad.getCargo();
        double weightInPounds = sumWeightInPounds(cargo);
        double weightInKilograms = sumWeightInKilograms(cargo);

        return new Weight(weightInPounds, weightInKilograms);
    }

    private Weight getBaggageWeight(int flightNumber, Date departureDate) {
        List<FlightEntity> flights = JsonMapper.mapJsonToFlightEntity(FLIGHT_ENTITY_JSON_FILE_PATH);
        List<FlightLoadEntity> flightsLoadList = JsonMapper.mapJsonToCargoEntity(CARGO_ENTITY_JSON_FILE_PATH);

        FlightEntity flight = flights.stream()
                .filter(f -> f.getFlightNumber() == flightNumber && f.getDepartureDate().equals(departureDate))
                .findFirst()
                .orElseThrow(() -> new NoSuchFlightException("Provided Flight Number or Departure Date is incorrect."));

        FlightLoadEntity flightLoad = flightsLoadList.stream()
                .filter(fl -> fl.getFlightId() == flight.getFlightId())
                .findFirst()
                .orElseThrow(() -> new NoSuchFlightException("Provided Flight Number is incorrect."));

        Baggage[] baggage = flightLoad.getBaggage();
        double weightInPounds = sumWeightInPounds(baggage);
        double weightInKilograms = sumWeightInKilograms(baggage);

        return new Weight(weightInPounds, weightInKilograms);
    }

    private <T>Double sumWeightInPounds(Freight[] freight) {
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

    private <T>Double sumWeightInKilograms(Freight[] freight) {
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
}
