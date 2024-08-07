package com.login.register.service.impl;

import com.login.register.Dto.UserProfileRequestDto;
import com.login.register.Repository.UserProfileRepo;
import com.login.register.model.UserProfile;
import com.login.register.service.EmailService;
import com.login.register.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

@Service
public class UserService implements UserProfileService {

    @Autowired
    private UserProfileRepo userProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void registerUser(String path,UserProfileRequestDto userProfileRequestDto) throws IOException {

        // Validation
        if (userProfileRepository.findByUsername(userProfileRequestDto.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userProfileRepository.findByEmail(userProfileRequestDto.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        if (userProfileRequestDto.getFullName() == null || userProfileRequestDto.getFullName().length() < 3) {
            throw new RuntimeException("Full name is not valid");
        }
        if (userProfileRequestDto.getPassword() == null || !(userProfileRequestDto.getPassword().contains("@") || userProfileRequestDto.getPassword().contains("!"))) {
            throw new RuntimeException("Password is not valid");
        }
        if (userProfileRequestDto.getEmail() == null || !userProfileRequestDto.getEmail().endsWith("@gmail.com")) {
            throw new RuntimeException("Email is not valid");
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

//    public void loginUser(String username, String password) {
//        UserProfile userProfile = userProfileRepository.findByUsername(username);
//        if (userProfile != null && userProfile.getPassword().equals(password) ) {
//            if(userProfile.isEnabled()){
//                System.out.println("Logged in");
//            }
//            throw new RuntimeException("User not verified");
//        }
//        throw new RuntimeException("User not found");
//    }
}
