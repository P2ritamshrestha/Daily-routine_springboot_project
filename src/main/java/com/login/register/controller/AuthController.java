package com.login.register.controller;

import com.login.register.Dto.UserProfileRequestDto;
import com.login.register.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserProfileService userProfileService;

    @Value("${Profile.image}")
    private String path;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerUser( @ModelAttribute UserProfileRequestDto userProfileRequestDto) throws IOException {
        userProfileService.registerUser(path,userProfileRequestDto);
        return new  ResponseEntity<>("User registered successfully. Please check your email for OTP.", HttpStatus.CREATED);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean verified = userProfileService.verifyOtp(email, otp);
        if (verified) {
            return ResponseEntity.ok("Email verified successfully. You can now login.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
//        return ResponseEntity.ok("Login successful");
//    }
}
