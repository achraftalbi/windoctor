package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.TestEntity2;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TestEntity2 entity.
 */
public interface TestEntity2Repository extends JpaRepository<TestEntity2,Long> {

}
