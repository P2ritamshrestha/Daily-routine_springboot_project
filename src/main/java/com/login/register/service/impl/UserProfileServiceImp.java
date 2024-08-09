package com.login.register.service.impl;

import com.login.register.Dto.JwtAuthenticationResponse;
import com.login.register.Dto.RefreshTokenRequest;
import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileRequestDto;
import com.login.register.Repository.UserProfileRepo;
import com.login.register.model.UserProfile;
import com.login.register.service.EmailService;
import com.login.register.service.JwtService;
import com.login.register.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImp implements UserProfileService {

    private final UserProfileRepo userProfileRepository;

    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userProfileRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            }
        };
    }




}
