package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Event;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
public interface EventRepository extends JpaRepository<Event,Long> {
    @Query("select e from Event e where e.event_date between ?1 and ?2")
    Page<Event> findAll(DateTime selectedDate, DateTime segondDate, Pageable var1);
    @Query("select e from Event e where e.user.id = ?1")
    List<Event> findByPatient(Long patientId);
}
