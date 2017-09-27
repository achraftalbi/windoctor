package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Prescription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Prescription entity.
 */
public interface PrescriptionSearchRepository extends ElasticsearchRepository<Prescription, Long> {
}
