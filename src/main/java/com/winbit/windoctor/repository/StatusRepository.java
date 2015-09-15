package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Status;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Status entity.
 */
public interface StatusRepository extends JpaRepository<Status,Long> {

}
