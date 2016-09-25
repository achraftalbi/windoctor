package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Plan;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Plan entity.
 */
public interface PlanSearchRepository extends ElasticsearchRepository<Plan, Long> {
}
