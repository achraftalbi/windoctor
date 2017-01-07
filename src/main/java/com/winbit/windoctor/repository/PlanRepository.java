package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Plan;
import org.springframework.data.jpa.repository.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spring Data JPA repository for the Plan entity.
 */
public interface PlanRepository extends JpaRepository<Plan,Long> {

    @Query("select count(p) from Plan p where p.structure.id = ?2 and p.user_id = ?1")
    BigDecimal findPlanNumber(Long patientId, Long structureId);

}
