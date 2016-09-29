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

    @Query("select t from Treatment t where (t.event).id = ?1 and (t.status.id is null or t.status.id=3)")
    Page<Treatment> findByEvent(Long idEvent, Pageable var1);

    @Query("select t from Treatment t where t.event.user.id = ?1 and (t.status.id is null or t.status.id=3)")
    Page<Treatment> findByPatient(Long patientId, Pageable var1);

    @Query("select t from Treatment t where t.event.user.structure.id = ?3 and (t.treatment_date between ?1 and ?2) and (?4 is null or t.doctor.id=?4) and (t.status.id is null or t.status.id=3) order by t.treatment_date asc")
    Page<Treatment> findByFirstLastDatesDoctor(DateTime firstDate, DateTime lastDate, Long structure_id, Long doctorId, Pageable var1);

    @Query("select new Treatment(sum(t.price),sum(t.paid_price)) from Treatment t where t.event.user.id = ?1  and (t.status.id is null or t.status.id=3) order by t.treatment_date asc")
    Treatment findTotalPatientTreatments(Long patientId);

    @Query("select new Treatment(sum(t.price),sum(t.paid_price)) from Treatment t where t.event.user.structure.id = ?3 and (t.treatment_date between ?1 and ?2) and (?4 is null or t.doctor.id=?4) and (t.status.id is null or t.status.id=3) order by t.treatment_date asc")
    Treatment findTotalByFirstLastDatesDoctor(DateTime firstDate, DateTime lastDate, Long structure_id, Long doctorId);

    @Query("select t from Treatment t where t.event.user.id = ?2 and (t.status.id is null or t.status.id=3) and " +
        " ( lower(t.description) like lower(?1) or lower(t.eventReason.description) like lower(?1) or lower(t.price) like lower(?1) or lower(t.paid_price) like lower(?1)) ")
    Page<Treatment> findAllMatchString(String query, Long patientId, Pageable var1);

}
