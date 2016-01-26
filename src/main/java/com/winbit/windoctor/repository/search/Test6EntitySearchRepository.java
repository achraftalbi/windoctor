package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Test6Entity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Test6Entity entity.
 */
public interface Test6EntitySearchRepository extends ElasticsearchRepository<Test6Entity, Long> {
}
