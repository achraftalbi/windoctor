package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Fund_history;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Fund_history entity.
 */
public interface Fund_historySearchRepository extends ElasticsearchRepository<Fund_history, Long> {
}
