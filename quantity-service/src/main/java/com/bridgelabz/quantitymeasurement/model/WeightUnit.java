package com.bridgelabz.quantitymeasurement.model;

/**
 * Enum representing weight units and their base conversion factors (Base: GRAM).
 */
public enum WeightUnit implements IUnit{
    GRAM(1.0),
    KILOGRAM(1000.0),
    TONNE(1000000.0);

    private final double baseConversionFactor;

    WeightUnit(double baseConversionFactor) {
        this.baseConversionFactor = baseConversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * this.baseConversionFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.baseConversionFactor;
    }
}