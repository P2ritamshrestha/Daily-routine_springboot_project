package com.login.register.service;

import com.login.register.Dto.JwtAuthenticationResponse;
import com.login.register.Dto.RefreshTokenRequest;
import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileRequestDto;

import java.io.IOException;

public interface AuthenticationService {
    void registerUser(String path, UserProfileRequestDto userProfileRequestDto) throws IOException;
    boolean verifyOtp(String email, String otp);
    JwtAuthenticationResponse signIn(SignInRequest signInRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
