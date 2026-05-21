package com.bridgelabz.quantitymeasurement.model;

/**
 * Interface to enforce conversion behavior across different measurement categories.
 */
public interface IUnit {
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
}