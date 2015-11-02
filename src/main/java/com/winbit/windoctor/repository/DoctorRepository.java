package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Doctor;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Doctor entity.
 */
public interface DoctorRepository extends JpaRepository<Doctor,Long> {

}
