package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.OperationRequestDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityRequestDTO;
import com.bridgelabz.quantitymeasurement.dto.QuantityResponseDTO;
import com.bridgelabz.quantitymeasurement.entity.QuantityRecord;
import com.bridgelabz.quantitymeasurement.service.QuantityMeasurementService;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quantity")
public class QuantityMeasurementController {

    @Autowired
    private QuantityMeasurementService service;

    @GetMapping("/health")
    public ResponseEntity<?> check(){
        QuantityResponseDTO response = new QuantityResponseDTO();
        response.setMessage("health check ok");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert")
    public ResponseEntity<QuantityResponseDTO> convert(
            @Valid @RequestBody QuantityRequestDTO request,
            @RequestParam String targetUnit,@RequestHeader("X-User-Email") String email) {
        // errors are handled globally in GlobalExceptionHandler, no try-catch needed here
        QuantityResponseDTO response = service.convertQuantity(request, targetUnit,email);
        return ResponseEntity.ok(response);
    }
    // compare quantities
    @PostMapping("/compare")
    public ResponseEntity<Boolean> compare(@Valid @RequestBody OperationRequestDTO request, @RequestHeader("X-User-Email") String email) {
        boolean areEqual = service.compareQuantities(request, email);
        return ResponseEntity.ok(areEqual);
    }
    // add quantities
    @PostMapping("/add")
    public ResponseEntity<QuantityResponseDTO> add(@Valid @RequestBody OperationRequestDTO request, @RequestHeader("X-User-Email") String email) {
        QuantityResponseDTO response = service.addQuantities(request, email);
        return ResponseEntity.ok(response);
    }

    // subtract quantities
    @PostMapping("/subtract")
    public ResponseEntity<QuantityResponseDTO> subtract(@Valid @RequestBody OperationRequestDTO request, @RequestHeader("X-User-Email") String email) {
        QuantityResponseDTO response = service.subtractQuantities(request, email);
        return ResponseEntity.ok(response);
    }

    // divide quantity by a number
    @PostMapping("/divide")
    public ResponseEntity<QuantityResponseDTO> divide(@Valid @RequestBody OperationRequestDTO request, @RequestHeader("X-User-Email") String email) {
        QuantityResponseDTO response = service.divideQuantity(request, email);
        return ResponseEntity.ok(response);
    }

    // get history
    @GetMapping("/history")
    public ResponseEntity<List<QuantityRecord>> getHistory(@RequestHeader("X-User-Email") String email) {
        List<QuantityRecord> history = service.getHistory(email);
        return ResponseEntity.ok(history);
    }

    // clear history
    @DeleteMapping("/history")
    public ResponseEntity<QuantityResponseDTO> clearHistory(@RequestHeader("X-User-Email") String email) {
        service.clearHistory(email);
        return ResponseEntity.ok(new QuantityResponseDTO(0, "", "History cleared successfully"));
    }

}