package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.EntityTest1;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EntityTest1 entity.
 */
public interface EntityTest1Repository extends JpaRepository<EntityTest1,Long> {

}
