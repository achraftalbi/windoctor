package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Category;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("select c from Category c where c.structure.id = ?1")
    Page<Category> findAll(Long structure_id, Pageable var1);
}
