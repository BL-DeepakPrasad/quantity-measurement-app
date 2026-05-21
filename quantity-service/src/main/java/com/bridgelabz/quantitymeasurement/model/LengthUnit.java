package com.bridgelabz.quantitymeasurement.model;

/**
 * Enum representing different units of measurement and their base conversion factors.
 * All units are normalized to a base unit (e.g., INCH for Length) to facilitate arithmetic.
 */
public enum LengthUnit implements IUnit{
    FEET(12.0),
    INCH(1.0),
    YARD(36.0),
    CENTIMETER(1.0 / 2.54),   // 1 cm is exactly 1/2.54 inches
    METER(100.0 / 2.54);      // 1 meter is 100 cm

    private final double baseConversionFactor;

    /**
     * Constructor to initialize the unit with its base conversion factor.
     * * @param baseConversionFactor the multiplier to convert this unit to the base unit.
     */
    LengthUnit(double baseConversionFactor) {
        this.baseConversionFactor = baseConversionFactor;
    }

    /**
     * Converts a given value of this unit into the normalized base unit.
     * * @param value the numeric value to convert.
     * @return the converted value in the base unit.
     */
    public double convertToBaseUnit(double value) {
        return value * this.baseConversionFactor;
    }

    /**
     * Converts a base unit value back into this specific unit.
     * @param baseValue the numeric value in the base unit (INCH).
     * @return the converted value in this unit.
     */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.baseConversionFactor;
    }

}