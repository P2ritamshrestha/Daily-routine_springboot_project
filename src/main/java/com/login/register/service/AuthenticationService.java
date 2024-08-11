package com.login.register.service;

import com.login.register.Dto.JwtAuthenticationResponse;
import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileRequestDto;

import java.io.IOException;
import java.io.InputStream;

public interface AuthenticationService {
    void registerUser(String path, UserProfileRequestDto userProfileRequestDto) throws IOException;
    boolean verifyOtp(String email, String otp);
    UserProfileRequestDto signIn(SignInRequest signInRequest);
    InputStream getUserDetail(String path, Integer id) throws IOException;
}
