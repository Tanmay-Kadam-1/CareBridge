package com.carebridge.patientservice.service;

import billing.BillingResponse;
import com.carebridge.patientservice.Exception.EmailAlreadyExistsException;
import com.carebridge.patientservice.Exception.PatientNotFoundException;
import com.carebridge.patientservice.dto.PatientRequestDTO;
import com.carebridge.patientservice.dto.PatientResponseDTO;
import com.carebridge.patientservice.grpc.BillingServiceGrpcClient;
import com.carebridge.patientservice.mapper.PatientMapper;
import com.carebridge.patientservice.model.Patient;
import com.carebridge.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient)
    {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public List<PatientResponseDTO> getPatients()
    {
        List<Patient> patients = patientRepository.findAll();

        return patients
                .stream()
                .map(PatientMapper::toDTO)
                .toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO)
    {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) throw new EmailAlreadyExistsException("This Email address is already registered for another Patient" + patientRequestDTO.getEmail());

        Patient patientNew  = patientRepository.save( PatientMapper.toModel(patientRequestDTO) );

        billingServiceGrpcClient.createBillingAccount(patientNew.getId().toString(),patientNew.getName(),patientNew.getEmail());

        return PatientMapper.toDTO(patientNew);
    }

    public PatientResponseDTO updatePatient(UUID id,
                                            PatientRequestDTO patientRequestDTO) {

        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),
                id)) {
            throw new EmailAlreadyExistsException(
                    "A patient with this email " + "already exists"
                            + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

}
