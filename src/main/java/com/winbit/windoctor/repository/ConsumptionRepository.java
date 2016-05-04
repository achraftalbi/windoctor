package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Consumption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Consumption entity.
 */
public interface ConsumptionRepository extends JpaRepository<Consumption,Long> {

    @Query("select new Consumption(coalesce(sum(c.amount),0)) from Consumption c where c.consumption_product.id=?1")
    Consumption findTotalConsumption(Long productId);

    @Query("select c from Consumption c where c.consumption_product.id = ?1 order by c.creation_date desc")
    Page<Consumption> findAll(Long productId, Pageable var1);

}
