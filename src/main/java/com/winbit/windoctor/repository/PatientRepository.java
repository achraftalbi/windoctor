package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Patient;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Patient entity.
 */
public interface PatientRepository extends JpaRepository<Patient,Long> {

}
