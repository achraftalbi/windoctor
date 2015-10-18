package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.domain.User;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Structure entity.
 */
public interface StructureRepository extends JpaRepository<Structure,Long> {

    Structure findOneById(Long id);

}
