package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Structure;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Structure entity.
 */
public interface StructureSearchRepository extends ElasticsearchRepository<Structure, Long> {
}
