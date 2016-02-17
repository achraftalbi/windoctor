package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Spring Data ElasticSearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
    @Query("{" +
        "\"filtered\":{\"query\":{\"multi_match\":{\"fields\" : [\"login\", \"firstName\", \"lastName\", \"email\"],\"query\":\"*?0*\"}},\"filter\":{\"term\":{\"structure.id\":\"?2\",\"authorities.name\":\"?1\"}}}" +
        "}")
    /*@Query("{" +
        "\"bool\":{\"must\":{\"term\":{\"structure.id\":\"?2\",\"authorities.name\":\"?1\"}}}" +
        "}")*/
    List<User> search(String query,String role,Long structure_id);
}
