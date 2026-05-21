package com.bridgelabz.quantitymeasurement.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityRequestDTO {

    @NotBlank(message = "Quantity type (LENGTH, VOLUME, etc.) is required.")
    private String quantityType;

    @NotNull(message = "The numerical value is required.")
    private Double value;

    @NotBlank(message = "The unit type cannot be blank.")
    private String unit;
}