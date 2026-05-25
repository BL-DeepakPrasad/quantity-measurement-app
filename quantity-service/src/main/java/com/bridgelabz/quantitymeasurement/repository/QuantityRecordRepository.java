package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityRecord;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantityRecordRepository extends JpaRepository<QuantityRecord, Long> {
    List<QuantityRecord> findByUserEmailOrderByIdDesc(String userEmail);

    @Transactional
    void deleteByUserEmail(String userEmail);
}