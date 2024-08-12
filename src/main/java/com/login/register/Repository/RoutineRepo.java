package com.login.register.Repository;

import com.login.register.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepo extends JpaRepository<Routine, Integer> {

}
