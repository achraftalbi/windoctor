package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Fund;
import com.winbit.windoctor.domain.Supply_type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Supply_type entity.
 */
public interface Supply_typeRepository extends JpaRepository<Supply_type,Long> {
    @Query("select s from Supply_type s where s.structure.id = ?1 order by s.id asc")
    Page<Supply_type> findAll(Long structure_id, Pageable var1);
}
