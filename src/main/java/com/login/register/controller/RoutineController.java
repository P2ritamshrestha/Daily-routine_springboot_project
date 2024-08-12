package com.login.register.controller;

import com.login.register.Dto.RoutineDto;
import com.login.register.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/routine")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    public ResponseEntity<Map<String, String>> saveRoutine(@RequestBody RoutineDto routineDto) {
        routineService.saveRoutine(routineDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
