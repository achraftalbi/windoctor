package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Prescription_medication;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prescription_medication entity.
 */
public interface Prescription_medicationRepository extends JpaRepository<Prescription_medication,Long> {

}
