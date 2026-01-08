package com.carebridge.authservice.dto;

/*
 * ============================
 * REGISTER REQUEST DTO
 * ============================
 *
 * This DTO represents the payload sent by the client
 * when creating a new user.
 *
 * IMPORTANT:
 * - Password is plain text ONLY here
 * - It must NEVER be stored as plain text
 * - Encryption happens in the service layer
 */
public class RegisterRequestDTO {

    private String email;
    private String password;
    private String role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * Plain-text password received from client.
     * This value will be encrypted before saving.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
     * Role example:
     * - USER
     * - ADMIN
     */
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}