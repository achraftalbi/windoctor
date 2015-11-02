package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Treatment;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Treatment entity.
 */
public interface TreatmentRepository extends JpaRepository<Treatment,Long> {
    @Query("select t from Treatment t where (t.event).id = ?1 ")
    Page<Treatment> findByEvent(Long idEvent, Pageable var1);
    @Query("select t from Treatment t where t.event.user.id = ?1 ")
    Page<Treatment> findByPatient(Long patientId, Pageable var1);
}
