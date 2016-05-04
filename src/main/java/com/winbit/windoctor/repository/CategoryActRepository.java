package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.CategoryAct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CategoryAct entity.
 */
public interface CategoryActRepository extends JpaRepository<CategoryAct,Long> {
    @Query("select c from CategoryAct c where c.structure.id = ?1")
    Page<CategoryAct> findAll(Long structure_id, Pageable var1);
    @Query("select c from CategoryAct c  where c.structure.id = ?2 and" +
        " ( lower(c.name) like lower(?1) ) ")
    Page<CategoryAct> findAllMatchString(String query , Long structureId, Pageable var1);
}
