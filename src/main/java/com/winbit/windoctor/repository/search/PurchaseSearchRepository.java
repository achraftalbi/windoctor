package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Purchase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Purchase entity.
 */
public interface PurchaseSearchRepository extends ElasticsearchRepository<Purchase, Long> {
}
