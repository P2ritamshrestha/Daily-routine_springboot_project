package com.login.register.controller;

import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileRequestDto;
import com.login.register.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    public ResponseEntity<Map<String, String>> registerUser(@ModelAttribute UserProfileRequestDto userProfileRequestDto) throws IOException {
        authenticationService.registerUser(path, userProfileRequestDto);

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
    public ResponseEntity<UserProfileRequestDto> signIn(@RequestBody SignInRequest signInRequest) {
        UserProfileRequestDto userProfile = authenticationService.signIn(signInRequest);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userProfile);
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        try {
            String basePath = "path/to/your/image/directory"; // Replace with your actual base path
            InputStream imageStream = authenticationService.getProfilePicture(basePath, id);

            byte[] imageBytes = IOUtils.toByteArray(imageStream);
            imageStream.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
