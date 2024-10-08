package com.login.register.controller;

import com.login.register.Dto.RoutineDto;
import com.login.register.Dto.ShiftDto;
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
    public ResponseEntity<Map<String, List<String>>> getShiftByUserId(@PathVariable Integer id) {
        List<String> shifts = routineService.getShiftByUserId(id);
        Map<String, List<String>> response = new HashMap<>();
        response.put("shifts", shifts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{shift}/{id}")
    public ResponseEntity<List<RoutineDto>> getRoutineByShift(@PathVariable Shift shift, @PathVariable Integer id) {
        return new ResponseEntity<>(routineService.getRoutineByShift(shift, id),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteRoutine(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        response.put("msg",routineService.deleteRoutineById(id));
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String,String>> updateRoutine(@PathVariable Integer id, @RequestBody RoutineDto routineDto) {
        routineService.updateRoutineById(id,routineDto);
        Map<String, String> response = new HashMap<>();
        response.put("msg", "Update Success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
