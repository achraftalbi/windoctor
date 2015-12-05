package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Supply_type;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Supply_type entity.
 */
public interface Supply_typeSearchRepository extends ElasticsearchRepository<Supply_type, Long> {
}
