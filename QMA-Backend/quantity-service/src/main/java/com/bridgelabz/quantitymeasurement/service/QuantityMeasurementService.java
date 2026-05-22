package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.OperationRequestDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityRequestDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityResponseDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityRecord;

import com.bridgelabz.quantitymeasurement.exception.QuantityMeasurementException;
import com.bridgelabz.quantitymeasurement.model.*;
import com.bridgelabz.quantitymeasurement.repository.QuantityRecordRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class QuantityMeasurementService {

    @Autowired
    private QuantityRecordRepository repository;



    // ==========================================
    // UNIT RESOLUTION — single place to map type + string → IUnit enum
    // ==========================================
    private IUnit resolveUnit(String type, String unitStr) {
        switch (type) {
            case "LENGTH":      return LengthUnit.valueOf(unitStr);
            case "VOLUME":      return VolumeUnit.valueOf(unitStr);
            case "WEIGHT":      return WeightUnit.valueOf(unitStr);
            case "TEMPERATURE": return TemperatureUnit.valueOf(unitStr);
            default:
                throw new QuantityMeasurementException("Invalid Quantity Type. Use LENGTH, VOLUME, WEIGHT, or TEMPERATURE.");
        }
    }

    // ==========================================
    // CONVERT
    // ==========================================
    public QuantityResponseDTO convertQuantity(QuantityRequestDTO request, String targetUnitStr, String email) {
        log.info("converting: type={}, value={}, from={}, to={}",
                request.getQuantityType(), request.getValue(), request.getUnit(), targetUnitStr);
        try {
            String type = request.getQuantityType().toUpperCase();
            String inputUnitStr = request.getUnit().toUpperCase();
            targetUnitStr = targetUnitStr.toUpperCase();

            IUnit inputUnit = resolveUnit(type, inputUnitStr);
            IUnit targetUnit = resolveUnit(type, targetUnitStr);

            double baseValue = inputUnit.convertToBaseUnit(request.getValue());
            double resultValue = targetUnit.convertFromBaseUnit(baseValue);

            // save the conversion to the database so we have a history
            QuantityRecord record = new QuantityRecord();
            record.setUserEmail(email);
            record.setQuantityType(type);
            record.setOperation("CONVERT");
            record.setInputValue(request.getValue());
            record.setInputUnit(inputUnitStr);
            record.setTargetUnit(targetUnitStr);
            record.setResultValue(resultValue);
            repository.save(record);

            log.info("done. result={} {}", resultValue, targetUnitStr);

            return new QuantityResponseDTO(resultValue, targetUnitStr, "Conversion Successful & Saved to DB!");

        } catch (IllegalArgumentException e) {
            log.error("conversion failed, bad unit spelling: {}", e.getMessage());
            throw new QuantityMeasurementException("Failed to convert: Invalid unit spelling for the given category.");
        }
    }

    // ==========================================
    // COMPARE
    // ==========================================
    public boolean compareQuantities(OperationRequestDTO request, String email) {
        String type = request.getQuantityType().toUpperCase();
        IUnit unit1 = resolveUnit(type, request.getFirstUnit().toUpperCase());
        IUnit unit2 = resolveUnit(type, request.getSecondUnit().toUpperCase());

        Quantity<IUnit> q1 = Quantity.of(request.getFirstValue(), unit1);
        Quantity<IUnit> q2 = Quantity.of(request.getSecondValue(), unit2);
        boolean areEqual = q1.equals(q2);

        QuantityRecord record = new QuantityRecord();
        record.setUserEmail(email);
        record.setQuantityType(type);
        record.setOperation("COMPARE");
        record.setInputValue(request.getFirstValue());
        record.setInputUnit(request.getFirstUnit().toUpperCase());
        record.setSecondValue(request.getSecondValue());
        record.setSecondUnit(request.getSecondUnit().toUpperCase());
        record.setResultString(areEqual ? "EQUAL" : "NOT EQUAL");
        repository.save(record);

        return areEqual;
    }

    // ==========================================
    // ADD
    // ==========================================
    public QuantityResponseDTO addQuantities(OperationRequestDTO request, String email) {
        return performTwoQuantityOperation(request, email, "ADD", Quantity::add);
    }

    // ==========================================
    // SUBTRACT
    // ==========================================
    public QuantityResponseDTO subtractQuantities(OperationRequestDTO request, String email) {
        return performTwoQuantityOperation(request, email, "SUBTRACT", Quantity::subtract);
    }

    // ==========================================
    // DIVIDE
    // ==========================================
    public QuantityResponseDTO divideQuantity(OperationRequestDTO request, String email) {
        String type = request.getQuantityType().toUpperCase();
        String targetStr = request.getTargetUnit() != null ? request.getTargetUnit().toUpperCase() : request.getFirstUnit().toUpperCase();

        IUnit inputUnit = resolveUnit(type, request.getFirstUnit().toUpperCase());
        IUnit targetUnit = resolveUnit(type, targetStr);

        Quantity<IUnit> q = Quantity.of(request.getFirstValue(), inputUnit);
        Quantity<IUnit> result = q.divide(request.getSecondValue(), targetUnit);

        QuantityRecord record = new QuantityRecord();
        record.setUserEmail(email);
        record.setQuantityType(type);
        record.setOperation("DIVIDE");
        record.setInputValue(request.getFirstValue());
        record.setInputUnit(request.getFirstUnit().toUpperCase());
        record.setSecondValue(request.getSecondValue());
        record.setSecondUnit(request.getFirstUnit().toUpperCase()); // division by a scalar effectively, but we record it
        record.setTargetUnit(targetStr);
        record.setResultValue(result.getValue());
        repository.save(record);

        return new QuantityResponseDTO(result.getValue(), targetStr, "Division Successful!");
    }

    // ==========================================
    // SHARED HELPER — eliminates duplication between add & subtract
    // ==========================================
    @FunctionalInterface
    private interface TwoQuantityOp {
        Quantity<IUnit> apply(Quantity<IUnit> q1, Quantity<IUnit> q2, IUnit targetUnit);
    }

    private QuantityResponseDTO performTwoQuantityOperation(OperationRequestDTO request, String email, String opName, TwoQuantityOp operation) {
        String type = request.getQuantityType().toUpperCase();
        String targetStr = request.getTargetUnit() != null ? request.getTargetUnit().toUpperCase() : request.getFirstUnit().toUpperCase();

        IUnit unit1 = resolveUnit(type, request.getFirstUnit().toUpperCase());
        IUnit unit2 = resolveUnit(type, request.getSecondUnit().toUpperCase());
        IUnit targetUnit = resolveUnit(type, targetStr);

        Quantity<IUnit> q1 = Quantity.of(request.getFirstValue(), unit1);
        Quantity<IUnit> q2 = Quantity.of(request.getSecondValue(), unit2);
        Quantity<IUnit> result = operation.apply(q1, q2, targetUnit);

        QuantityRecord record = new QuantityRecord();
        record.setUserEmail(email);
        record.setQuantityType(type);
        record.setOperation(opName);
        record.setInputValue(request.getFirstValue());
        record.setInputUnit(request.getFirstUnit().toUpperCase());
        record.setSecondValue(request.getSecondValue());
        record.setSecondUnit(request.getSecondUnit().toUpperCase());
        record.setTargetUnit(targetStr);
        record.setResultValue(result.getValue());
        repository.save(record);

        return new QuantityResponseDTO(result.getValue(), targetStr, opName + " Successful!");
    }

    // ==========================================
    // HISTORY
    // ==========================================
    public List<QuantityRecord> getHistory(String email) {
        return repository.findByUserEmailOrderByIdDesc(email);
    }

    public void clearHistory(String email) {
        repository.deleteByUserEmail(email);
    }
}