package com.login.register.service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UserProfileService {
       UserDetailsService userDetailsService();
}
