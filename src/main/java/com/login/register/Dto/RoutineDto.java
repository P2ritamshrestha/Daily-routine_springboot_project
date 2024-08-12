package com.login.register.Dto;

import com.login.register.model.Shift;
import com.login.register.model.UserProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
@Setter
public class RoutineDto {

    private String title;

    private String description;

    private LocalTime startTime;

    private LocalTime endTime;

    private Shift shiftingTime;

    private Integer userProfileId;

}
