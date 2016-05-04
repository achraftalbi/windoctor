package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.CategoryAct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CategoryAct entity.
 */
public interface CategoryActSearchRepository extends ElasticsearchRepository<CategoryAct, Long> {
}
