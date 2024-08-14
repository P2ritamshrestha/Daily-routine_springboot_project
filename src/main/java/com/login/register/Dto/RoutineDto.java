package com.login.register.Dto;

import com.login.register.model.Shift;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
@Setter
public class RoutineDto {

    private Integer id;
    private String title;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Shift shiftingTime;
    private Integer userProfileId;
    private Long hour;

}
