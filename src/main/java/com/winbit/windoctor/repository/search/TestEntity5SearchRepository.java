package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.TestEntity5;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TestEntity5 entity.
 */
public interface TestEntity5SearchRepository extends ElasticsearchRepository<TestEntity5, Long> {
}
