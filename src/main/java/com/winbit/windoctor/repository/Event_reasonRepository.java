package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Event_reason;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Event_reason entity.
 */
public interface Event_reasonRepository extends JpaRepository<Event_reason,Long> {

}
