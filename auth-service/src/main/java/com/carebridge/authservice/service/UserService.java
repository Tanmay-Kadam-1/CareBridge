package com.carebridge.authservice.service;

import com.carebridge.authservice.model.User;
import com.carebridge.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public Optional<User> findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    public Optional<User> createUser(String email, String rawPassword, String role)
    {
        // Prevent duplicate users
        if (userRepository.existsByEmail(email)) {
            return Optional.empty();
        }

        User user = new User();
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        return Optional.of(userRepository.save(user));
    }
}
