package com.login.register.service;

import com.login.register.Dto.RoutineDto;
import com.login.register.Dto.ShiftDto;
import com.login.register.model.Routine;
import com.login.register.model.Shift;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RoutineService {

    void saveRoutine(RoutineDto routineDto);
    List<String> getShiftByUserId(Integer id);

    List<RoutineDto> getRoutineByShift(Shift shift, Integer id);

    Routine getRoutineById(Integer id);

    String deleteRoutineById(Integer id);

    void updateRoutineById(Integer id, RoutineDto routineDto);
}
