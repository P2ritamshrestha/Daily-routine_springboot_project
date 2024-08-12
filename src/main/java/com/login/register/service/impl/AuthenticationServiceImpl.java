package com.login.register.service.impl;

import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileRequestDto;
import com.login.register.Repository.UserProfileRepo;
import com.login.register.model.UserProfile;
import com.login.register.service.AuthenticationService;
import com.login.register.service.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserProfileRepo userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserProfileRepo userRepository;
    private final AuthenticationManager authenticationManager;

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
    public UserProfileRequestDto signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        UserProfile user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()){
            throw new RuntimeException("Otp not verified");
        }
        UserProfileRequestDto userProfileRequestDto = new UserProfileRequestDto();
        userProfileRequestDto.setId(user.getId());
        userProfileRequestDto.setFullName(user.getFullName());
        userProfileRequestDto.setUsername(user.getUsername());

        return userProfileRequestDto;
    }

    @Override
    public InputStream getProfilePicture(String basePath, Integer id) throws IOException {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User profile not found for id: " + id));

        String profilePath = userProfile.getProfilePath();
        if (profilePath == null || profilePath.isEmpty()) {
            throw new FileNotFoundException("Profile picture path is not set for user id: " + id);
        }

        Path imagePath = Paths.get(basePath, profilePath);

        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("File does not exist: " + imagePath);
        }

        if (!Files.isReadable(imagePath)) {
            throw new AccessDeniedException("Cannot read file: " + imagePath);
        }

        return Files.newInputStream(imagePath);
    }
}
