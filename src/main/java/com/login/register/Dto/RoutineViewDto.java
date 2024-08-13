package com.login.register.Dto;

import com.login.register.model.Shift;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineViewDto {
    private Integer id;
    private String title;
    private Shift shiftingTime;
    private Long hour;
}
