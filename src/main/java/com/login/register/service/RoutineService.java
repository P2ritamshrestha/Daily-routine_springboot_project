package com.login.register.service;

import com.login.register.Dto.RoutineDto;
import com.login.register.Dto.ShiftDto;
import com.login.register.model.Routine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoutineService {

    void saveRoutine(RoutineDto routineDto);
    List<String> getShiftByUserId(Integer id);

    List<RoutineDto> getRoutineByShift(ShiftDto shiftDto, Integer id);

    Routine getRoutineById(Integer id);
}
