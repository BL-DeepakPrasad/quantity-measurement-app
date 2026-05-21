package com.bridgelabz.quantitymeasurement.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class QuantityRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String quantityType;
    private double inputValue;
    private String inputUnit;
    private String targetUnit;
    private double resultValue;
    private String userEmail;
}