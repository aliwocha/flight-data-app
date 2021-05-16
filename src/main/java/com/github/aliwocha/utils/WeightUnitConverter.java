package com.github.aliwocha.utils;

public class WeightUnitConverter {

    private static final double KILOGRAM_TO_POUND_RATIO = 2.20462262;

    public static double toPounds(double kg) {
        return kg * KILOGRAM_TO_POUND_RATIO;
    }

    public static double toKilograms(double lb) {
        return lb / KILOGRAM_TO_POUND_RATIO;
    }
}
