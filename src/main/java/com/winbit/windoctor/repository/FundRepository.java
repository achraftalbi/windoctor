package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Fund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fund entity.
 */
public interface FundRepository extends JpaRepository<Fund,Long> {
    @Query("select f from Fund f where f.structure.id = ?1 order by f.id asc")
    Page<Fund> findAll(Long structureId, Pageable var1);
}
