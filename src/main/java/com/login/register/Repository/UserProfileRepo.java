package com.login.register.Repository;

import com.login.register.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
    UserProfile findByUsername(String username);
    UserProfile findByEmail(String email);
}
