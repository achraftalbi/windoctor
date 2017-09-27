package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Prescription_medication;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Prescription_medication entity.
 */
public interface Prescription_medicationSearchRepository extends ElasticsearchRepository<Prescription_medication, Long> {
}
