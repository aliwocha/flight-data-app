package com.github.aliwocha.app;

public class FlightDataApp {

    public static void main(String[] args) {

        FlightDataAppController appController = new FlightDataAppController();
        appController.controlLoop();
    }
}
