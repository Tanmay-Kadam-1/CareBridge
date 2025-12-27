package com.carebridge.patientservice.service;

import com.carebridge.patientservice.dto.PatientResponseDTO;
import com.carebridge.patientservice.mapper.PatientMapper;
import com.carebridge.patientservice.model.Patient;
import com.carebridge.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository)
    {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients()
    {
        List<Patient> patients = patientRepository.findAll();

        return patients
                .stream()
                .map(PatientMapper::toDTO)
                .toList();
    }
}
