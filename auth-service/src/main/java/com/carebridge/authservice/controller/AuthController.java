package com.carebridge.authservice.controller;

import com.carebridge.authservice.dto.LoginRequestDTO;
import com.carebridge.authservice.dto.LoginResponseDTO;
import com.carebridge.authservice.dto.RegisterRequestDTO;
import com.carebridge.authservice.dto.RegisterResponseDTO;
import com.carebridge.authservice.model.User;
import com.carebridge.authservice.service.AuthService;
import com.carebridge.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService,
                          UserService userService)
    {
        this.authService = authService;
        this.userService = userService;
    }

    // ========================= LOGIN =========================

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO)
    {
        Optional<String> tokenOptional =
                authService.authenticate(loginRequestDTO);

        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
                new LoginResponseDTO(tokenOptional.get())
        );
    }

    // ========================= REGISTER =========================

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @RequestBody RegisterRequestDTO request)
    {
        System.out.println("HELOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        Optional<User> createdUser =
                userService.createUser(
                        request.getEmail(),
                        request.getPassword(),
                        request.getRole()
                );

        if (createdUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build(); // email already exists
        }

        User user = createdUser.get();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponseDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getRole()
                ));
    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        // Authorization: Bearer <token>
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}