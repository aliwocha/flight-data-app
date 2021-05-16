package com.github.aliwocha.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aliwocha.model.FlightLoadEntity;
import com.github.aliwocha.model.FlightEntity;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

public class JsonMapper {

    public static List<FlightEntity> mapJsonToFlightEntity(String filePath) {
        List<FlightEntity> flightEntityList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<FlightEntity>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(filePath);

        try {
            flightEntityList = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            System.err.println("Unable to read file");
            e.printStackTrace();
        }

        return flightEntityList;
    }

    public static List<FlightLoadEntity> mapJsonToCargoEntity(String filePath) {
        List<FlightLoadEntity> cargoEntityList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<FlightLoadEntity>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(filePath);

        try {
            cargoEntityList = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            System.err.println("Unable to read file");
            e.printStackTrace();
        }

        return cargoEntityList;
    }
}
