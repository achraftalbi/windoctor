package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.TestEntity4;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TestEntity4 entity.
 */
public interface TestEntity4SearchRepository extends ElasticsearchRepository<TestEntity4, Long> {
}
