package com.example.onlinebanking.controller;

import com.example.onlinebanking.dto.UserDTO;
import com.example.onlinebanking.model.User;
import com.example.onlinebanking.service.UserService;
import com.example.onlinebanking.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OTPService otpService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(convertToDTO(createdUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ResponseEntity.ok(convertToDTO(value)))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                                      .map(this::convertToDTO)
                                      .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String customerId, @RequestParam String password) {
        if (userService.authenticate(customerId, password)) {
            return ResponseEntity.ok("Logged in successfully");
        } else {
            return ResponseEntity.status(401).body("Invalid customer ID or password");
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String phoneNumber) {
        otpService.sendOtp(phoneNumber);
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(phoneNumber, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP validated successfully");
        } else {
            return ResponseEntity.status(401).body("Invalid OTP");
        }
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        userService.requestPasswordReset(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                      .id(user.getId())
                      .customerId(user.getCustomerId())
                      .name(user.getName())
                      .email(user.getEmail())
                      .phone(user.getPhone())
                      .aadharNumber(user.getAadharNumber())
                      .panNumber(user.getPanNumber())
                      .houseNumber(user.getHouseNumber())
                      .city(user.getCity())
                      .state(user.getState())
                      .pincode(user.getPincode())
                      .createdDate(user.getCreatedDate())
                      .build();
    }
}
