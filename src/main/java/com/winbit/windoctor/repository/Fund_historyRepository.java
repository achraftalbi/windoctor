package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Fund_history;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fund_history entity.
 */
public interface Fund_historyRepository extends JpaRepository<Fund_history,Long> {
    @Query("select new Fund_history(f) from Fund_history f where f.fund.id = ?1 order by f.creation_date asc")
    Page<Fund_history> findAll(Long fundId, Pageable var1);
}
