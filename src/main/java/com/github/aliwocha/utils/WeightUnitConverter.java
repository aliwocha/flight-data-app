package com.github.aliwocha.utils;

public class WeightUnitConverter {

    private static final double KILOGRAM_TO_POUND_RATIO = 2.20462262;

    public static double toPounds(final double kg) {
        if (kg > 0) {
            return kg * KILOGRAM_TO_POUND_RATIO;
        } else {
            return 0;
        }
    }

    public static double toKilograms(final double lb) {
        if (lb > 0) {
            return lb / KILOGRAM_TO_POUND_RATIO;
        } else {
            return 0;
        }
    }
}
