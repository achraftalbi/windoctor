package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Fund;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Fund entity.
 */
public interface FundSearchRepository extends ElasticsearchRepository<Fund, Long> {
}
