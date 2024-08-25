package com.login.register.service;

import com.login.register.Dto.SignInRequest;
import com.login.register.Dto.UserProfileDto;
import org.springframework.core.io.Resource;

import java.io.IOException;

public interface AuthenticationService {
    void registerUser(UserProfileDto userProfileDto) throws IOException;
    boolean verifyOtp(String email, String otp);
    UserProfileDto signIn(SignInRequest signInRequest);
    Resource getImageAsResource(String imageName) throws IOException;

    UserProfileDto getProfileDetail(Integer id);

    String updateProfile(Integer id, UserProfileDto userProfileDto);
}
