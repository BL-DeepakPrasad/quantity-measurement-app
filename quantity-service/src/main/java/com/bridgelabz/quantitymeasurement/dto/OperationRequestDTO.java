package com.bridgelabz.quantitymeasurement.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationRequestDTO {


    @NotBlank(message = "Quantity type is required.")
    private String quantityType;

    @NotNull(message = "First value is required.")
    private Double firstValue;

    @NotBlank(message = "First unit is required.")
    private String firstUnit;

    @NotNull(message = "Second value is required.")
    private Double secondValue;

    @NotBlank(message = "Second unit is required.")
    private String secondUnit;


    private String targetUnit;
}