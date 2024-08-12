package com.login.register.service.impl;

import com.login.register.Dto.RoutineDto;
import com.login.register.Repository.RoutineRepo;
import com.login.register.Repository.UserProfileRepo;
import com.login.register.model.Routine;
import com.login.register.model.UserProfile;
import com.login.register.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        UserProfile userProfile= userProfileRepo.findById(routineDto.getUserProfileId()).get();
        routine.setUserProfile(userProfile);
        //Save routine
        routineRepo.save(routine);
    }
}
