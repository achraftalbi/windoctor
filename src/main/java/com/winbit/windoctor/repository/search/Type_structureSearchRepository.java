package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Type_structure;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Type_structure entity.
 */
public interface Type_structureSearchRepository extends ElasticsearchRepository<Type_structure, Long> {
}
