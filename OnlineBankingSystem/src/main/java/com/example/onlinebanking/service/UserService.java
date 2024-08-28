package com.example.onlinebanking.service;

import com.example.onlinebanking.model.PasswordResetToken;
import com.example.onlinebanking.model.User;
import com.example.onlinebanking.repository.PasswordResetTokenRepository;
import com.example.onlinebanking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException("Phone number is already taken");
        }
        String customerId = generateCustomerId();
        while (userRepository.existsByCustomerId(customerId)) {
            customerId = generateCustomerId();
        }
        user.setCustomerId(customerId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    private String generateCustomerId() {
        Random random = new Random();
        return String.format("%09d", random.nextInt(1_000_000_000));
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean authenticate(String customerId, String password) {
        Optional<User> userOpt = userRepository.findByCustomerId(customerId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public void requestPasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expirationDate(LocalDateTime.now().plusHours(1)) // Token valid for 1 hour
                    .build();
            passwordResetTokenRepository.save(resetToken);
            // Send the token to the user via email or SMS
            // Implement the email/SMS sending logic here
        }
    }

    public void resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByToken(token);
        if (tokenOpt.isPresent()) {
            PasswordResetToken resetToken = tokenOpt.get();
            if (resetToken.getExpirationDate().isAfter(LocalDateTime.now())) {
                User user = resetToken.getUser();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                passwordResetTokenRepository.delete(resetToken); // Remove the token after use
            } else {
                throw new IllegalArgumentException("Token has expired");
            }
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}
