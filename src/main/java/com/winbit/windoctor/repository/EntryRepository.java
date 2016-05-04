package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Entry entity.
 */
public interface EntryRepository extends JpaRepository<Entry,Long> {
    @Query("select new Entry(coalesce(sum(p.price*p.amount),0),coalesce(sum(p.amount),0)) from Entry p where p.charge.id=?1")
    Entry findTotalCharge(Long chargeId);

    @Query("select p from Entry p where p.charge.id = ?1 order by p.creation_date desc")
    Page<Entry> findAll(Long chargeId, Pageable var1);
}
