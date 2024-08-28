package com.example.onlinebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 9)
    private String customerId;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false, unique = true)
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    @NotBlank(message = "Aadhar card number is required")
    @Size(min = 12, max = 12, message = "Aadhar card number should be 12 digits")
    @Column(nullable = false, unique = true)
    private String aadharNumber;

    @NotBlank(message = "PAN number is required")
    @Size(min = 10, max = 10, message = "PAN number should be 10 characters")
    @Column(nullable = false, unique = true)
    private String panNumber;

    @NotBlank(message = "House number is required")
    private String houseNumber;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    @Size(min = 6, max = 6, message = "Pincode should be 6 digits")
    private String pincode;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
}
