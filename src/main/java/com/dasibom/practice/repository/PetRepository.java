package com.dasibom.practice.repository;

import com.dasibom.practice.domain.Pet;
import com.dasibom.practice.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    Optional<Pet> findByPetNameAndOwner(String petName, User owner);
}
