package com.login.register.Dto;

import com.login.register.model.Shift;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDto {
    private Integer id;
    private Shift shift;
}
