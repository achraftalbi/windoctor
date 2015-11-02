package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Treatment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Treatment entity.
 */
public interface TreatmentSearchRepository extends ElasticsearchRepository<Treatment, Long> {
}
