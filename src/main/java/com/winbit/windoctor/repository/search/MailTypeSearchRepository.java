package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.MailType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MailType entity.
 */
public interface MailTypeSearchRepository extends ElasticsearchRepository<MailType, Long> {
}
