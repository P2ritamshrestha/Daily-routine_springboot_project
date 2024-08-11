package com.login.register.service.impl;

import com.login.register.Dto.JwtAuthenticationResponse;
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public InputStream getUserDetail(String path, Integer id) throws IOException {
        UserProfile userProfile =  userProfileRepository.findById(id).get();
        String Path = userProfile.getProfilePath();

        String fullPath = path + File.separator ;
        return new FileInputStream(fullPath);
    }

}
