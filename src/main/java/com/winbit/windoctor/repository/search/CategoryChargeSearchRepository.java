package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.CategoryCharge;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CategoryCharge entity.
 */
public interface CategoryChargeSearchRepository extends ElasticsearchRepository<CategoryCharge, Long> {
}
