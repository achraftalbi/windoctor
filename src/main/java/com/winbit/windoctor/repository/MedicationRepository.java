package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Medication;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Medication entity.
 */
public interface MedicationRepository extends JpaRepository<Medication,Long> {

}
