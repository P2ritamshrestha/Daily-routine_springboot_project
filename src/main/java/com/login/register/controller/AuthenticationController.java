package com.login.register.controller;

import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileDto;
import com.login.register.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @Value("${Profile.image}")
    private String path;

    @PostMapping(value = "/signUp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> registerUser(@ModelAttribute UserProfileDto userProfileDto) throws IOException {
        authenticationService.registerUser(userProfileDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully. Please check your email for OTP.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean verified = authenticationService.verifyOtp(email, otp);
        if (verified) {
            return ResponseEntity.ok("Email verified successfully. You can now login.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserProfileDto> signIn(@RequestBody SignInRequest signInRequest) {
        UserProfileDto userProfile = authenticationService.signIn(signInRequest);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userProfile);
    }


    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
        Resource imageResource = authenticationService.getImageAsResource(imageName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName + "\"")
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(imageResource);
    }



}
