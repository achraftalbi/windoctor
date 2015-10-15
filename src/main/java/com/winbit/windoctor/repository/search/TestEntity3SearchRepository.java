package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.TestEntity3;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TestEntity3 entity.
 */
public interface TestEntity3SearchRepository extends ElasticsearchRepository<TestEntity3, Long> {
}
