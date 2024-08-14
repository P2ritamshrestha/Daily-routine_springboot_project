package com.login.register.Repository;

import com.login.register.Dto.ShiftDto;
import com.login.register.model.Routine;
import com.login.register.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepo extends JpaRepository<Routine, Integer> {
    @Query(value = "SELECT DISTINCT r.shifting_time FROM routine r WHERE r.user_id = :id", nativeQuery = true)
    Optional<List<String>> getShiftByUserId(Integer id);

    @Query(value = "SELECT * FROM Routine r WHERE r.shifting_time= :shift AND r.user_id= :id", nativeQuery = true)
    List<Routine> getRoutineByShift(String shift, Integer id);
}
