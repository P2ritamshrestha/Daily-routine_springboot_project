package com.login.register.service.impl;

import com.login.register.Dto.JwtAuthenticationResponse;
import com.login.register.Dto.RefreshTokenRequest;
import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileRequestDto;
import com.login.register.Repository.UserProfileRepo;
import com.login.register.model.UserProfile;
import com.login.register.service.AuthenticationService;
import com.login.register.service.EmailService;
import com.login.register.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserProfileRepo userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserProfileRepo userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public void registerUser(String path, UserProfileRequestDto userProfileRequestDto) throws IOException {

        // Validation
            Optional<UserProfile> userProfileByUsername = userProfileRepository.findByUsername(userProfileRequestDto.getUsername());
            if (userProfileByUsername.isPresent()) {
                throw new RuntimeException("Username already exists");
            }

        if (userProfileRepository.findByEmail(userProfileRequestDto.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
//        if (userProfileRequestDto.getFullName() == null || userProfileRequestDto.getFullName().length() < 3) {
//            throw new RuntimeException("Full name is not valid");
//        }
//        if (userProfileRequestDto.getPassword() == null || !(userProfileRequestDto.getPassword().contains("@") || userProfileRequestDto.getPassword().contains("!"))) {
//            throw new RuntimeException("Password is not valid");
//        }
//        if (userProfileRequestDto.getEmail() == null || !userProfileRequestDto.getEmail().endsWith("@gmail.com")) {
//            throw new RuntimeException("Email is not valid");
//        }

        String filepath= path+ File.separator+ userProfileRequestDto.getProfile().getOriginalFilename();
        File newFile = new File(path);
        if(!newFile.exists()){
            newFile.mkdirs();
        }
        Files.copy(userProfileRequestDto.getProfile().getInputStream(), Paths.get(filepath));


        UserProfile userProfile = UserProfile.builder()
                .fullName(userProfileRequestDto.getFullName())
                .password(passwordEncoder.encode(userProfileRequestDto.getPassword()))
                .username(userProfileRequestDto.getUsername())
                .email(userProfileRequestDto.getEmail())
                .otpCode(generateOtp())
                .profilePath(filepath).build();

        userProfileRepository.save(userProfile);
        emailService.sendOtpEmail(userProfileRequestDto.getEmail(), userProfile.getOtpCode());

    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        UserProfile userProfile = userProfileRepository.findByEmail(email);
        if (userProfile != null && userProfile.getOtpCode().equals(otp)) {
            userProfile.setEnabled(true);
            userProfile.setOtpCode(null);
            userProfileRepository.save(userProfile);
            return true;
        }
        return false;
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        var user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()){
            throw new RuntimeException("Otp not verified");
        }

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefereshToken(new HashMap<>(),user);

        JwtAuthenticationResponse jwtAuthenticationResponse= new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken((String) refreshToken);
        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String username= jwtService.extractUserName(refreshTokenRequest.getToken());
        UserProfile user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(jwtService.isValidToken(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse= new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }

}
