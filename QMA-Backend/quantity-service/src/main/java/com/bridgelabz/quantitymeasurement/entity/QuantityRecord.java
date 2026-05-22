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
    private String operation; // e.g., CONVERT, COMPARE, ADD, SUBTRACT, DIVIDE
    private double inputValue;
    private String inputUnit;
    
    // For 2-input operations
    private Double secondValue;
    private String secondUnit;
    
    private String targetUnit;
    private double resultValue;
    private String resultString; // Store formatted result like "EQUAL"

    // --- NEW: Store email instead of User entity ---
    private String userEmail;
}