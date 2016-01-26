package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Test6Entity;
import com.winbit.windoctor.repository.Test6EntityRepository;
import com.winbit.windoctor.repository.search.Test6EntitySearchRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Test6Entity.
 */
@RestController
@RequestMapping("/api")
public class Test6EntityResource {

    private final Logger log = LoggerFactory.getLogger(Test6EntityResource.class);

    @Inject
    private Test6EntityRepository test6EntityRepository;

    @Inject
    private Test6EntitySearchRepository test6EntitySearchRepository;

    /**
     * POST  /test6Entitys -> Create a new test6Entity.
     */
    @RequestMapping(value = "/test6Entitys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Test6Entity> create(@Valid @RequestBody Test6Entity test6Entity) throws URISyntaxException {
        log.debug("REST request to save Test6Entity : {}", test6Entity);
        if (test6Entity.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new test6Entity cannot already have an ID").body(null);
        }
        Test6Entity result = test6EntityRepository.save(test6Entity);
        test6EntitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/test6Entitys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("test6Entity", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /test6Entitys -> Updates an existing test6Entity.
     */
    @RequestMapping(value = "/test6Entitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Test6Entity> update(@Valid @RequestBody Test6Entity test6Entity) throws URISyntaxException {
        log.debug("REST request to update Test6Entity : {}", test6Entity);
        if (test6Entity.getId() == null) {
            return create(test6Entity);
        }
        Test6Entity result = test6EntityRepository.save(test6Entity);
        test6EntitySearchRepository.save(test6Entity);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("test6Entity", test6Entity.getId().toString()))
                .body(result);
    }

    /**
     * GET  /test6Entitys -> get all the test6Entitys.
     */
    @RequestMapping(value = "/test6Entitys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Test6Entity>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Test6Entity> page = test6EntityRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test6Entitys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /test6Entitys/:id -> get the "id" test6Entity.
     */
    @RequestMapping(value = "/test6Entitys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Test6Entity> get(@PathVariable Long id) {
        log.debug("REST request to get Test6Entity : {}", id);
        return Optional.ofNullable(test6EntityRepository.findOne(id))
            .map(test6Entity -> new ResponseEntity<>(
                test6Entity,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /test6Entitys/:id -> delete the "id" test6Entity.
     */
    @RequestMapping(value = "/test6Entitys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Test6Entity : {}", id);
        test6EntityRepository.delete(id);
        test6EntitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("test6Entity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/test6Entitys/:query -> search for the test6Entity corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/test6Entitys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Test6Entity> search(@PathVariable String query) {
        return StreamSupport
            .stream(test6EntitySearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
