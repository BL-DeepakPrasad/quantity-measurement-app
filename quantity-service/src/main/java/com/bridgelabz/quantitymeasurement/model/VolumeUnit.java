package com.bridgelabz.quantitymeasurement.model;

/**
 * Enum representing volume units and their base conversion factors (Base: LITRE).
 */
public enum VolumeUnit implements IUnit {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double baseConversionFactor;

    VolumeUnit(double baseConversionFactor) {
        this.baseConversionFactor = baseConversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * this.baseConversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.baseConversionFactor;
    }
}