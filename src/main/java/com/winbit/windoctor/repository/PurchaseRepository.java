package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Purchase entity.
 */
public interface PurchaseRepository extends JpaRepository<Purchase,Long> {
    @Query("select new Purchase(coalesce(sum(p.price*p.amount),0),coalesce(sum(p.amount),0)) from Purchase p where p.purchase_product.id=?1")
    Purchase findTotalProduct(Long productId);

    @Query("select p from Purchase p where p.purchase_product.id = ?1 order by p.creation_date desc")
    Page<Purchase> findAll(Long productId, Pageable var1);
}
