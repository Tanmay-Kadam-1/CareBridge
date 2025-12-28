package com.carebridge.patientservice.repository;

import com.carebridge.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByEmail(String Email);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
