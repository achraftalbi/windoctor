package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    @Query("select a from Authority a where a.priority >= ?1")
    List<Authority> findAllUnderPriority(Integer priority);
}
