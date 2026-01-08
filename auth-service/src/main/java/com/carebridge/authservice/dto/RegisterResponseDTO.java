package com.carebridge.authservice.dto;

import java.util.UUID;

/*
 * ============================
 * REGISTER RESPONSE DTO
 * ============================
 *
 * Returned after successful user creation.
 *
 * IMPORTANT:
 * - Never return password
 * - Safe fields only
 */
public class RegisterResponseDTO {

    private UUID userId;
    private String email;
    private String role;

    public RegisterResponseDTO(UUID userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}