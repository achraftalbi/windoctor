package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Event_reason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Event_reason entity.
 */
public interface Event_reasonRepository extends JpaRepository<Event_reason,Long> {
    @Query("select e from Event_reason e where e.structure.id = ?1")
    Page<Event_reason> findAll(Long structure_id, Pageable var1);
    @Query("select e from Event_reason e  where e.structure.id = ?2 and" +
        " ( lower(e.description) like lower(?1) or lower(e.price) like lower(?1) ) ")
    Page<Event_reason> findAllMatchString(String query , Long structureId, Pageable var1);
}
