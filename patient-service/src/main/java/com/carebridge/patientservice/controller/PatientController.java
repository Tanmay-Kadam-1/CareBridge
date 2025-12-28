package com.carebridge.patientservice.controller;

import com.carebridge.patientservice.dto.PatientRequestDTO;
import com.carebridge.patientservice.dto.PatientResponseDTO;
import com.carebridge.patientservice.dto.validators.CreatePatientValidationGroup;
import com.carebridge.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(
        name = "Patient",
        description = "APIs for managing patient records"
)
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Get all patients")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of patients"
    )
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        return ResponseEntity.ok(patientService.getPatients());
    }

    @Operation(summary = "Create a new patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Patient with same email already exists")
    })
    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO
    ) {
        return ResponseEntity.ok(patientService.createPatient(patientRequestDTO));
    }

    @Operation(summary = "Update an existing patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @Parameter(
                    description = "Unique identifier of the patient",
                    required = true,
                    example = "c1b0a7a1-2b5e-4f47-8f62-92e6dcb2b9a1"
            )
            @PathVariable UUID id,

            @Validated(Default.class)
            @RequestBody PatientRequestDTO patientRequestDTO
    ) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDTO));
    }

    @Operation(summary = "Delete a patient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @Parameter(
                    description = "Unique identifier of the patient",
                    required = true,
                    example = "c1b0a7a1-2b5e-4f47-8f62-92e6dcb2b9a1"
            )
            @PathVariable UUID id
    ) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}