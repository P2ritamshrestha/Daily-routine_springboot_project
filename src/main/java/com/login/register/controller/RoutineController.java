package com.login.register.controller;

import com.login.register.Dto.RoutineDto;
import com.login.register.Dto.RoutineViewDto;
import com.login.register.Dto.ShiftDto;
import com.login.register.model.Routine;
import com.login.register.model.Shift;
import com.login.register.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<RoutineDto>> getRoutine(@PathVariable Integer id) {
        return new ResponseEntity<>(routineService.getRoutineByUserId(id),HttpStatus.OK);
    }

    @GetMapping("/shift")
    public ResponseEntity<List<RoutineViewDto>> getRoutineByShift(@RequestBody ShiftDto shiftDto) {
        return new ResponseEntity<>(routineService.getRoutineByShift(shiftDto),HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<Routine> getRoutineById(@RequestParam Integer id) {
        return new ResponseEntity<>(routineService.getRoutineById(id),HttpStatus.OK);
    }


}
