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

    public static List<FlightEntity> mapJsonToFlightEntity(final String filePath) {
        List<FlightEntity> flightEntityList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<FlightEntity>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(filePath);

        try {
            flightEntityList = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            System.err.println("Unable to read file!");
            e.printStackTrace();
        }

        return flightEntityList;
    }

    public static List<FlightLoadEntity> mapJsonToFlightLoadEntity(final String filePath) {
        List<FlightLoadEntity> flightLoadEntityList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<FlightLoadEntity>> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(filePath);

        try {
            flightLoadEntityList = mapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            System.err.println("Unable to read file!");
            e.printStackTrace();
        }

        return flightLoadEntityList;
    }
}
