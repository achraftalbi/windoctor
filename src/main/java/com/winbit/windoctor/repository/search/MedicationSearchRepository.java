package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Medication;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Medication entity.
 */
public interface MedicationSearchRepository extends ElasticsearchRepository<Medication, Long> {
}
