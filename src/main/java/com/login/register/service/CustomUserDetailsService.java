package com.login.register.service;

import com.login.register.Repository.UserProfileRepo;
import com.login.register.model.UserProfile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserProfileRepo userProfileRepository;

    public CustomUserDetailsService(UserProfileRepo userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile userProfile = userProfileRepository.findByUsername(username);

        return org.springframework.security.core.userdetails.User
                .withUsername(userProfile.getUsername())
                .password(userProfile.getPassword())
                .roles("USER")
                .build();
    }
}
