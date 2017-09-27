package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Prescription;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prescription entity.
 */
public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {

}
