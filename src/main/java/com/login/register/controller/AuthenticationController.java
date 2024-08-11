package com.login.register.controller;

import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileRequestDto;
import com.login.register.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @Value("${Profile.image}")
    private String path;

    @PostMapping(value = "/signUp",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerUser( @ModelAttribute UserProfileRequestDto userProfileRequestDto) throws IOException {
        authenticationService.registerUser(path,userProfileRequestDto);
        return new  ResponseEntity<>("User registered successfully. Please check your email for OTP.", HttpStatus.CREATED);
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


//    @PostMapping("/refresh")
//    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
//        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
//    }

    @GetMapping(value = "/images/{id}",produces = MediaType.IMAGE_JPEG_VALUE )
    public void downloadImage(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        InputStream resource= authenticationService.getUserDetail(path,id);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
