package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Event_reason;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Event_reason entity.
 */
public interface Event_reasonSearchRepository extends ElasticsearchRepository<Event_reason, Long> {
}
