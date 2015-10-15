package com.winbit.windoctor.web.rest.mapper;

import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.web.rest.dto.TestEntity2DTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TestEntity2 and its DTO TestEntity2DTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TestEntity2Mapper {

    TestEntity2DTO testEntity2ToTestEntity2DTO(TestEntity2 testEntity2);

    @Mapping(target = "testEntity3s", ignore = true)
    TestEntity2 testEntity2DTOToTestEntity2(TestEntity2DTO testEntity2DTO);
}
