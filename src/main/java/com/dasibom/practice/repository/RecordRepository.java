package com.dasibom.practice.repository;

import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.Record;
import com.dasibom.practice.domain.StampType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

    Optional<Record> findByPetAndStampType(Pet pet, StampType stampType);
}
