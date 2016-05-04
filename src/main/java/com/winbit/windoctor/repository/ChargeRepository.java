package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Charge entity.
 */
public interface ChargeRepository extends JpaRepository<Charge,Long> {
    @Query("select p from Charge p where p.structure.id = ?1")
    Page<Charge> findAll(Long structure_id, Pageable var1);
    @Query("select p from Charge p, CategoryCharge c  where p.categoryCharge.id=c.id and p.structure.id = ?2 and" +
        " ( lower(p.name) like lower(?1) or lower(p.price) like lower(?1) or lower(p.amount) like lower(?1) " +
        "or lower(c.name) like lower(?1)) ")
    Page<Charge> findAllMatchString(String query , Long structureId, Pageable var1);
}
