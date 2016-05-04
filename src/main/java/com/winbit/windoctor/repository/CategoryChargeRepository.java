package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.CategoryCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CategoryCharge entity.
 */
public interface CategoryChargeRepository extends JpaRepository<CategoryCharge,Long> {
    @Query("select c from CategoryCharge c where c.structure.id = ?1")
    Page<CategoryCharge> findAll(Long structure_id, Pageable var1);
    @Query("select c from CategoryCharge c  where c.structure.id = ?2 and" +
        " ( lower(c.name) like lower(?1) ) ")
    Page<CategoryCharge> findAllMatchString(String query , Long structureId, Pageable var1);
}
