package com.login.register.Repository;

import com.login.register.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findByUsername(String username);
    UserProfile findByEmail(String email);

    @Query(value = "SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM users u WHERE u.username = ?1", nativeQuery = true)
    boolean existsByUsername(String username);

}
