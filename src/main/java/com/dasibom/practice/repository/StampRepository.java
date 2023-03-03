package com.dasibom.practice.repository;

import com.dasibom.practice.domain.Stamp;
import com.dasibom.practice.domain.StampType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    Optional<Stamp> findByStampType(StampType stampType);
}
