package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.EntityTest1;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EntityTest1 entity.
 */
public interface EntityTest1SearchRepository extends ElasticsearchRepository<EntityTest1, Long> {
}
