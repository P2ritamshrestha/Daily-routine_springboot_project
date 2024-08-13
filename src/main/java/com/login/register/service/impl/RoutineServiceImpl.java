package com.login.register.service.impl;

import com.login.register.Dto.RoutineDto;
import com.login.register.Dto.RoutineViewDto;
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
    public List<RoutineDto> getRoutineByUserId(Integer id) {
        List<Routine> routine = routineRepo.getRoutineByUserId(id)
                .orElseThrow(() -> new RuntimeException("Routine not found"));
        List<RoutineDto> routineList = new ArrayList<>();
        for (Routine route : routine) {
            RoutineDto routineDto = new RoutineDto();
            routineDto.setTitle(route.getTitle());
            routineDto.setDescription(route.getDescription());
            routineDto.setShiftingTime(route.getShiftingTime());
            routineDto.setStartTime(route.getStartTime());
            routineDto.setEndTime(route.getEndTime());
            routineDto.setUserProfileId(id);
            routineList.add(routineDto);
        }

        return routineList;
    }

    @Override
    public List<RoutineViewDto> getRoutineByShift(ShiftDto shiftDto) {

        String shiftString = shiftDto.getShift().toString();
        List<Routine> routine = routineRepo.getRoutineByShift(shiftString);
        List<RoutineViewDto> routineList = new ArrayList<>();
        for (Routine route : routine) {
            RoutineViewDto routineViewDto = new RoutineViewDto();
            routineViewDto.setId(route.getId());
            routineViewDto.setTitle(route.getTitle());
            routineViewDto.setShiftingTime(route.getShiftingTime());
            Duration duration = Duration.between(route.getStartTime(), route.getEndTime());
            Long hours = duration.toHours();
            routineViewDto.setHour(hours);

            routineList.add(routineViewDto);
        }
        return routineList;
    }

    @Override
    public Routine getRoutineById(Integer id) {
       return routineRepo.findById(id).orElseThrow(() -> new RuntimeException("Routine not found"));
    }

}
