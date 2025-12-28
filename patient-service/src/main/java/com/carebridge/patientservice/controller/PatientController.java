package com.carebridge.patientservice.controller;

import com.carebridge.patientservice.dto.PatientRequestDTO;
import com.carebridge.patientservice.dto.PatientResponseDTO;
import com.carebridge.patientservice.dto.validators.CreatePatientValidationGroup;
import com.carebridge.patientservice.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private PatientService patientService;

    public PatientController(PatientService patientService)
    {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients()
    {
        List<PatientResponseDTO> patientResponseDTOList = patientService.getPatients();
        return ResponseEntity.ok().body(patientResponseDTOList);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO)
    {
        return ResponseEntity.ok().body(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,@RequestBody @Validated({Default.class}) PatientRequestDTO patientRequestDTO)
    {
        return ResponseEntity.ok().body(patientService.updatePatient(id,patientRequestDTO));
    }

}
