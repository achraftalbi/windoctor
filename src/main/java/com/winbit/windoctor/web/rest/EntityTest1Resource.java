package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.EntityTest1;
import com.winbit.windoctor.repository.EntityTest1Repository;
import com.winbit.windoctor.repository.search.EntityTest1SearchRepository;
import com.winbit.windoctor.web.rest.util.HeaderUtil;
import com.winbit.windoctor.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing EntityTest1.
 */
@RestController
@RequestMapping("/api")
public class EntityTest1Resource {

    private final Logger log = LoggerFactory.getLogger(EntityTest1Resource.class);

    @Inject
    private EntityTest1Repository entityTest1Repository;

    @Inject
    private EntityTest1SearchRepository entityTest1SearchRepository;

    /**
     * POST  /entityTest1s -> Create a new entityTest1.
     */
    @RequestMapping(value = "/entityTest1s",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EntityTest1> create(@RequestBody EntityTest1 entityTest1) throws URISyntaxException {
        log.debug("REST request to save EntityTest1 : {}", entityTest1);
        if (entityTest1.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new entityTest1 cannot already have an ID").body(null);
        }
        EntityTest1 result = entityTest1Repository.save(entityTest1);
        entityTest1SearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/entityTest1s/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("entityTest1", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /entityTest1s -> Updates an existing entityTest1.
     */
    @RequestMapping(value = "/entityTest1s",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EntityTest1> update(@RequestBody EntityTest1 entityTest1) throws URISyntaxException {
        log.debug("REST request to update EntityTest1 : {}", entityTest1);
        if (entityTest1.getId() == null) {
            return create(entityTest1);
        }
        EntityTest1 result = entityTest1Repository.save(entityTest1);
        entityTest1SearchRepository.save(entityTest1);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("entityTest1", entityTest1.getId().toString()))
                .body(result);
    }

    /**
     * GET  /entityTest1s -> get all the entityTest1s.
     */
    @RequestMapping(value = "/entityTest1s",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EntityTest1>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<EntityTest1> page = entityTest1Repository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entityTest1s", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /entityTest1s/:id -> get the "id" entityTest1.
     */
    @RequestMapping(value = "/entityTest1s/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EntityTest1> get(@PathVariable Long id) {
        log.debug("REST request to get EntityTest1 : {}", id);
        return Optional.ofNullable(entityTest1Repository.findOne(id))
            .map(entityTest1 -> new ResponseEntity<>(
                entityTest1,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /entityTest1s/:id -> delete the "id" entityTest1.
     */
    @RequestMapping(value = "/entityTest1s/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete EntityTest1 : {}", id);
        entityTest1Repository.delete(id);
        entityTest1SearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("entityTest1", id.toString())).build();
    }

    /**
     * SEARCH  /_search/entityTest1s/:query -> search for the entityTest1 corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/entityTest1s/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EntityTest1> search(@PathVariable String query) {
        return StreamSupport
            .stream(entityTest1SearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
