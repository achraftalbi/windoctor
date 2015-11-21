package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from Product p where p.structure.id = ?1")
    Page<Product> findAll(Long structure_id, Pageable var1);
    @Query("select p from Product p where p.structure.id = ?1 and (p.threshold>0 and p.amount<p.threshold)")
    Page<Product> findAllThreshold(Long structure_id, Pageable var1);
}
