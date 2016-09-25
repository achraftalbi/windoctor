package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Plan;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Plan entity.
 */
public interface PlanRepository extends JpaRepository<Plan,Long> {

}
