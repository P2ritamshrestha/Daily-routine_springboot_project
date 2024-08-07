package com.login.register.service;

import com.login.register.Dto.UserProfileRequestDto;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserProfileService {
    public void registerUser(String path, UserProfileRequestDto userProfileRequestDto) throws IOException;
    public boolean verifyOtp(String email, String otp);
}
