package com.bridgelabz.quantitymeasurement.model;

public enum TemperatureUnit implements IUnit {
    CELSIUS,
    FAHRENHEIT;

    @Override
    public double convertToBaseUnit(double value) {
        // Converting TO Celsius in base unit
        if (this == FAHRENHEIT) {
            return (value - 32.0) * 5.0 / 9.0;
        }
        // If it's already Celsius, just return the value
        return value;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        // Converting FROM Celsius in base unit
        if (this == FAHRENHEIT) {
            return (baseValue * 9.0 / 5.0) + 32.0;
        }
        return baseValue;
    }
}