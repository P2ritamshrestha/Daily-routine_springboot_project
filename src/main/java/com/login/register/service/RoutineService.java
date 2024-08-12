package com.login.register.service;

import com.login.register.Dto.RoutineDto;
import org.springframework.stereotype.Service;

@Service
public interface RoutineService {

    void saveRoutine(RoutineDto routineDto);
}
