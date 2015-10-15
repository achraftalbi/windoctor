package com.winbit.windoctor.repository;

import com.winbit.windoctor.domain.TestEntity3;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TestEntity3 entity.
 */
public interface TestEntity3Repository extends JpaRepository<TestEntity3,Long> {

    @Query("select distinct testEntity3 from TestEntity3 testEntity3 left join fetch testEntity3.testEntity2s")
    List<TestEntity3> findAllWithEagerRelationships();

    @Query("select testEntity3 from TestEntity3 testEntity3 left join fetch testEntity3.testEntity2s where testEntity3.id =:id")
    TestEntity3 findOneWithEagerRelationships(@Param("id") Long id);

}
