package com.github.aliwocha.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aliwocha.model.CargoEntity;
import com.github.aliwocha.model.FlightEntity;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

public class JsonMapper {

    static final String FLIGHT_ENTITY_JSON_FILE_PATH = "/json/flightEntity.json";
    static final String CARGO_ENTITY_JSON_FILE_PATH = "/json/cargoEntity.json";

    public static List<FlightEntity> mapJsonToFlightEntity() {
        List<FlightEntity> flightEntityList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<FlightEntity>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(FLIGHT_ENTITY_JSON_FILE_PATH);

        try {
            flightEntityList = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            System.err.println("Unable to read file");
            e.printStackTrace();
        }

        return flightEntityList;
    }

    public static List<CargoEntity> mapJsonToCargoEntity() {
        List<CargoEntity> cargoEntityList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<CargoEntity>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(CARGO_ENTITY_JSON_FILE_PATH);

        try {
            cargoEntityList = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            System.err.println("Unable to read file");
            e.printStackTrace();
        }

        return cargoEntityList;
    }
}
