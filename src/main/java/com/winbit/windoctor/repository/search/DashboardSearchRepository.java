package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Dashboard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Dashboard entity.
 */
public interface DashboardSearchRepository extends ElasticsearchRepository<Dashboard, Long> {
}
