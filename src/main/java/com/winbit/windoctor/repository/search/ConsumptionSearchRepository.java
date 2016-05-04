package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Consumption;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Consumption entity.
 */
public interface ConsumptionSearchRepository extends ElasticsearchRepository<Consumption, Long> {
}
