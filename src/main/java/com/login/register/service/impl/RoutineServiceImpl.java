package com.login.register.service.impl;

import com.login.register.Dto.RoutineDto;
import com.login.register.Dto.ShiftDto;
import com.login.register.Repository.RoutineRepo;
import com.login.register.Repository.UserProfileRepo;
import com.login.register.model.Routine;
import com.login.register.model.Shift;
import com.login.register.model.UserProfile;
import com.login.register.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {


    private final UserProfileRepo userProfileRepo;
    private final RoutineRepo routineRepo;


    @Override
    public void saveRoutine(RoutineDto routineDto) {
        Routine routine = new Routine();
        routine.setTitle(routineDto.getTitle());
        routine.setDescription(routineDto.getDescription());
        routine.setShiftingTime(routineDto.getShiftingTime());
        routine.setStartTime(routineDto.getStartTime());
        routine.setEndTime(routineDto.getEndTime());
        UserProfile userProfile = userProfileRepo.findById(routineDto.getUserProfileId()).get();
        routine.setUserProfile(userProfile);
        //Save routine
        routineRepo.save(routine);
    }

    @Override
    public List<String> getShiftByUserId(Integer id) {
        List<String> shiftList = routineRepo.getShiftByUserId(id)
                .orElseThrow(() -> new RuntimeException("Shift not found"));


        return shiftList;
    }
    @Override
    public List<RoutineDto> getRoutineByShift(Shift shift, Integer id) {

        String shiftString = shift.toString();
        List<Routine> routine = routineRepo.getRoutineByShift(shiftString,id);
        List<RoutineDto> routineList = new ArrayList<>();
        for (Routine route : routine) {
            RoutineDto routineDto = new RoutineDto();
            routineDto.setTitle(route.getTitle());
            routineDto.setUserProfileId(id);
            routineDto.setDescription(route.getDescription());
            routineDto.setStartTime(route.getStartTime());
            routineDto.setEndTime(route.getEndTime());
            routineDto.setShiftingTime(route.getShiftingTime());
            Duration duration = Duration.between(route.getStartTime(), route.getEndTime());
            Long hours = duration.toHours();
            routineDto.setHour(hours);
            routineList.add(routineDto);
        }
        return routineList;
    }

    @Override
    public Routine getRoutineById(Integer id) {
       return routineRepo.findById(id).orElseThrow(() -> new RuntimeException("Routine not found"));
    }

}
