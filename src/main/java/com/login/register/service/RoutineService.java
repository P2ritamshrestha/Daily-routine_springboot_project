package com.login.register.service;

import com.login.register.Dto.RoutineDto;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public interface RoutineService {

    void saveRoutine(RoutineDto routineDto);
    List<RoutineDto> getRoutineByUserId(Integer id);
}
