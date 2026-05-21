package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityRecordRepository extends JpaRepository<QuantityRecord, Long> {

}