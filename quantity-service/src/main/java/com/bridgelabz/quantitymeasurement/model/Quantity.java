package com.bridgelabz.quantitymeasurement.model;

import lombok.Getter;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

// this class holds a value and a unit together, like "12 inches" or "100 celsius"
public class Quantity<T extends IUnit> {
    @Getter
    private final double value;
    private final T unit;

    // we use a small tolerance to handle floating point comparison (e.g. 0.00001 difference is fine)
    private static final double EPSILON = 0.001;

    // private so no one can create a Quantity directly, use Quantity.of() instead
    private Quantity(double value, T unit) {
        if (value < 0.0) {
            throw new IllegalArgumentException("Quantity value cannot be negative");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.value = value;
        this.unit = unit;
    }

    // use this to create a new quantity, e.g. Quantity.of(12.0, LengthUnit.INCH)
    public static <T extends  IUnit>Quantity of(double value, T unit) {
        return new Quantity(value, unit);
    }

    // adds this quantity to another and returns the result in the target unit
    public Quantity<T> add(Quantity<T> other, T targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
       return performOperation(other, targetUnit, Double::sum);
    }

    // helper to add two base unit values (not used externally)
    private double calculateSumInBaseUnit(Quantity other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot add a null quantity");
        }
        return this.unit.convertToBaseUnit(this.value) +
                other.unit.convertToBaseUnit(other.value);
    }

    // subtracts another quantity from this one
    public Quantity<T> subtract(Quantity<T> other, T targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        if (other == null) {
            throw new IllegalArgumentException("Cannot perform operation with a null quantity");
        }
        return performOperation(other, targetUnit, (a, b) -> a - b);
    }

    // divides this quantity by a plain number (not another quantity)
    public Quantity<T> divide(double divisor, T targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        if (divisor == 0.0) {
            throw new ArithmeticException("Cannot divide by zero");
        }

        double valInBase = this.unit.convertToBaseUnit(this.value);
        double targetValue = targetUnit.convertFromBaseUnit(valInBase / divisor);
        return Quantity.of(targetValue, targetUnit);
    }

    // shared method used by add and subtract - converts both to base unit, does math, converts back
    private Quantity<T> performOperation(Quantity<T> other, T targetUnit, DoubleBinaryOperator operation) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        if (other == null) {
            throw new IllegalArgumentException("Cannot perform operation with a null quantity");
        }

        // step 1: convert both values to the base unit (e.g. inches for length)
        double val1InBase = this.unit.convertToBaseUnit(this.value);
        double val2InBase = other.unit.convertToBaseUnit(other.value);

        // step 2: apply the math (add, subtract, etc.)
        double resultInBase = operation.applyAsDouble(val1InBase, val2InBase);

        // step 3: convert the result back to the unit the user asked for
        double targetValue = targetUnit.convertFromBaseUnit(resultInBase);
        return Quantity.of(targetValue, targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Quantity<?> quantity = (Quantity<?>) obj;

        // make sure we don't compare apples to oranges (e.g. 1 inch vs 1 gram)
        if (!this.unit.getClass().equals(quantity.unit.getClass())) {
            return false;
        }

        return areValuesEqual(this.unit.convertToBaseUnit(this.value),
                ((IUnit) quantity.unit).convertToBaseUnit(quantity.value));
    }

    private boolean areValuesEqual(double val1, double val2) {
        return Math.abs(val1 - val2) <= EPSILON;
    }

    @Override
    public int hashCode() {
        // include the unit type in the hash so 1 inch and 1 gram don't collide in maps
        return Objects.hash(Math.round(unit.convertToBaseUnit(value) * 1000.0) / 1000.0, unit.getClass());
    }

    @Override
    public String toString() {
        return "Quantity{value=" + value + ", unit=" + unit + "}";
    }

}