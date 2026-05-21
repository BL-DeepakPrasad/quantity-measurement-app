package com.bridgelabz.quantitymeasurement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityResponseDTO {
    private double resultValue;
    private String resultUnit;
    private String message;
}