package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.MailSetting;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MailSetting entity.
 */
public interface MailSettingSearchRepository extends ElasticsearchRepository<MailSetting, Long> {
}
