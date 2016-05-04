package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Entry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Entry entity.
 */
public interface EntrySearchRepository extends ElasticsearchRepository<Entry, Long> {
}
