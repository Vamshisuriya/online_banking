package com.example.onlinebanking.service;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

    public void sendOtp(String phoneNumber) {
        // Implementation to send an OTP to the phone number
        // This could involve integrating with an SMS provider
    }

    public boolean validateOtp(String phoneNumber, String otp) {
        // Implementation to validate the provided OTP
        // This could involve checking the OTP from a cache or database
        return true; // Example implementation, replace with actual validation
    }
}
