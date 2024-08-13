package com.login.register.service;

import com.login.register.Dto.RoutineDto;
import com.login.register.Dto.RoutineViewDto;
import com.login.register.Dto.ShiftDto;
import com.login.register.model.Routine;
import com.login.register.model.Shift;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public interface RoutineService {

    void saveRoutine(RoutineDto routineDto);
    List<RoutineDto> getRoutineByUserId(Integer id);

    List<RoutineViewDto> getRoutineByShift(ShiftDto shiftDto);

    Routine getRoutineById(Integer id);
}
