package com.winbit.windoctor.web.rest.mapper;

import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.web.rest.dto.TestEntity3DTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TestEntity3 and its DTO TestEntity3DTO.
 */
@Mapper(componentModel = "spring", uses = {TestEntity2Mapper.class, })
public interface TestEntity3Mapper {

    TestEntity3DTO testEntity3ToTestEntity3DTO(TestEntity3 testEntity3);

    TestEntity3 testEntity3DTOToTestEntity3(TestEntity3DTO testEntity3DTO);

    default TestEntity2 testEntity2FromId(Long id) {
        if (id == null) {
            return null;
        }
        TestEntity2 testEntity2 = new TestEntity2();
        testEntity2.setId(id);
        return testEntity2;
    }
}
