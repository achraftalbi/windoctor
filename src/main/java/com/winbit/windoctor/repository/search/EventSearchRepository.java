package com.winbit.windoctor.repository.search;

import com.winbit.windoctor.domain.Event;
import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
//import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data ElasticSearch repository for the Event entity.
 */

public interface EventSearchRepository extends ElasticsearchRepository<Event, Long> {

    //@Query("{\"bool\" : {\"must\" : [ {\"query_string\": {\"query\": \"?0\"}}, {\"filter\":{\"term\":{\"user.structure.id\":?2}}}]}}")
   @Query("{" +
        "\"filtered\":{\"query\":{\"query_string\":{\"query\":\"*?0*\"}},\"filter\":{\"term\":{\"user.structure.id\":\"?2\",\"eventStatus.id\":\"?1\"}}}" +
        "}")
       /* @Query("{" +
            "\"filtered\":{\"query\":{\"query_string\":{\"query\":\"*?0*\"}},\"filter\":{\"bool\" : {\"must_not\" : {\"term\":{\"user.structure.id\":\"?2\",\"eventStatus.id\":\"?1\"}}}}}" +
            "}")   */
   Page<Event> search(String query,Long statusType,Long structure_id,Pageable var2);

    /*@Query("{" +
        "\"filtered\":{\"query\":{\"query_string\":{\"query\":\"*?0*\"}},\"filter\":[{\"term\":{\"user.structure.id\":\"?3\"}},{\"range\":{\"event_date\":{\"gte\":\"?1\",\"lt\":\"?2\"}}}]}" +
        "}")*/
    @Query("{" +
        "\"bool\":{\"must\":[{\"query_string\":{\"query\":\"+?0+\"}},{\"term\":{\"user.structure.id\":\"?3\"}},{\"range\":{\"event_date\":{\"gte\":\"?1\",\"lt\":\"?2\"}}}]}" +
        "}")
    Page<Event> search(String query,DateTime selectedDate, DateTime segondDate,Long structure_id,Pageable var2);
}
