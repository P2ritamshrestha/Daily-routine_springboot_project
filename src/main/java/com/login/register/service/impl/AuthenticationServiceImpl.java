package com.login.register.service.impl;

import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileDto;
import com.login.register.Repository.UserProfileRepo;
import com.login.register.model.UserProfile;
import com.login.register.service.AuthenticationService;
import com.login.register.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserProfileRepo userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    @Value("${Profile.image}")
    private String path;


    @Override
    public void registerUser(UserProfileDto userProfileDto) throws IOException {

        // Validation
            Optional<UserProfile> userProfileByUsername = userProfileRepository.findByUsername(userProfileDto.getUsername());
            if (userProfileByUsername.isPresent()) {
                throw new RuntimeException("Username already exists");
            }

        if (userProfileRepository.findByEmail(userProfileDto.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        MultipartFile file = userProfileDto.getProfile();

        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File newFile = new File(path);
        if(!newFile.exists()){
            newFile.mkdirs();
        }
        Path filePath = Paths.get(path, uniqueFileName);

        Files.copy(file.getInputStream(), filePath);

        UserProfile userProfile = UserProfile.builder()
                .fullName(userProfileDto.getFullName())
                .password(passwordEncoder.encode(userProfileDto.getPassword()))
                .username(userProfileDto.getUsername())
                .email(userProfileDto.getEmail())
                .otpCode(generateOtp())
                .name(uniqueFileName)
                .profilePath(filePath.toString())
                .build();

        userProfileRepository.save(userProfile);
        emailService.sendOtpEmail(userProfileDto.getEmail(), userProfile.getOtpCode());
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
    public UserProfileDto signIn(SignInRequest signInRequest) {
        UserProfileDto userProfileDto = new UserProfileDto();

        if (!userProfileRepository.existsByUsername(signInRequest.getUsername())) {
            userProfileDto.setMessage("Invalid username");
            return userProfileDto;
        }

        UserProfile user = userProfileRepository.findByUsername(signInRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            userProfileDto.setMessage("Invalid password");
            return userProfileDto;
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        if (!user.isEnabled()) {
            throw new RuntimeException("Otp not verified");
        }

        userProfileDto.setId(user.getId());
        userProfileDto.setFullName(user.getFullName());
        userProfileDto.setUsername(user.getUsername());
        userProfileDto.setName(user.getName());
        return userProfileDto;
    }


    @Override
    public Resource getImageAsResource(String imageName) throws IOException {
        Path imagePath = Paths.get(path, imageName);
        if (Files.exists(imagePath)) {
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not find the image " + imageName + " on the server.");
            }
        } else {
            throw new FileNotFoundException("Could not find the image " + imageName + " on the server.");
        }
    }

    @Override
    public UserProfileDto getProfileDetail(Integer id) {
       UserProfile userProfile= userProfileRepository.findById(id).get();
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setFullName(userProfile.getFullName());
        userProfileDto.setUsername(userProfile.getUsername());
        userProfileDto.setEmail(userProfile.getEmail());
        return userProfileDto;
    }

    @Override
    public String updateProfile(Integer id, UserProfileDto userProfileDto) {
        UserProfile userProfile= userProfileRepository.findById(id).orElseThrow(RuntimeException::new);
        if(Objects.nonNull(userProfileDto.getFullName())) {
            userProfile.setFullName(userProfileDto.getFullName());
        }
        if(Objects.nonNull(userProfileDto.getUsername())) {
            userProfile.setUsername(userProfileDto.getUsername());
        }
        if(Objects.nonNull(userProfileDto.getPassword())) {
            userProfile.setPassword(passwordEncoder.encode(userProfileDto.getPassword()));
        }
        userProfileRepository.save(userProfile);
        return "Profile updated successfully";
    }
}
