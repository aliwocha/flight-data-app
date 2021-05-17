package com.github.aliwocha.app;

import com.github.aliwocha.service.FlightDataAppService;

import java.util.InputMismatchException;
import java.util.Scanner;

class FlightDataAppController {
    private static final int UNDEFINED = 0;
    private static final int OPTION1 = 1;
    private static final int OPTION2 = 2;
    private static final int EXIT = 3;

    private final Scanner scanner = new Scanner(System.in);
    private final FlightDataAppService service = new FlightDataAppService();

    void controlLoop() {
        int option = UNDEFINED;

        while (option != EXIT) {
            printOptions();
            option = chooseOption();

            switch (option) {
                case OPTION1:
                    service.printResultsForRequestedFlight();
                    break;
                case OPTION2:
                    service.printResultsForRequestedAirport();
                    break;
                case EXIT:
                    close();
                    break;
                default:
                    System.err.println("Chosen option is not correct. Please try again.");
            }
        }
    }

    private void printOptions() {
        System.out.println("Choose option:");
        System.out.println("1 - Calculate baggage and cargo weight for requested Flight Number and Departure Date");
        System.out.println("2 - Calculate number of flights and baggage for requested IATA Airport Code");
        System.out.println("3 - Exit the program");
    }

    private int chooseOption() {
        int option;

        try {
            option = scanner.nextInt();
        } catch (InputMismatchException e) {
            option = UNDEFINED;
        } finally {
            scanner.nextLine();
        }

        return option;
    }

    private void close() {
        scanner.close();
    }
}
