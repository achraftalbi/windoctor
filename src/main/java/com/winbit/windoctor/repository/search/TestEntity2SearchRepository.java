package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.TestEntity2;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TestEntity2 entity.
 */
public interface TestEntity2SearchRepository extends ElasticsearchRepository<TestEntity2, Long> {
}
