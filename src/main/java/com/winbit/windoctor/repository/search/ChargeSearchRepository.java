package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Charge;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Charge entity.
 */
public interface ChargeSearchRepository extends ElasticsearchRepository<Charge, Long> {
}
