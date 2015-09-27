package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.Structure;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Structure entity.
 */
public interface StructureRepository extends JpaRepository<Structure,Long> {

}
