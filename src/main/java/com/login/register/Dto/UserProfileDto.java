package com.login.register.Dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Getter
@Setter
public class UserProfileDto {

    private String fullName;
    private String email;
    private String username;
    private String password;
    private MultipartFile profile;

    //Sign In:
    private Integer id;
    private String name;
    private String message;
}

