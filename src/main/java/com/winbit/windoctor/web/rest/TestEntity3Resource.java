package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.TestEntity3;
import com.winbit.windoctor.repository.TestEntity3Repository;
import com.winbit.windoctor.repository.search.TestEntity3SearchRepository;
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
 * REST controller for managing TestEntity3.
 */
@RestController
@RequestMapping("/api")
public class TestEntity3Resource {

    private final Logger log = LoggerFactory.getLogger(TestEntity3Resource.class);

    @Inject
    private TestEntity3Repository testEntity3Repository;

    @Inject
    private TestEntity3SearchRepository testEntity3SearchRepository;

    /**
     * POST  /testEntity3s -> Create a new testEntity3.
     */
    @RequestMapping(value = "/testEntity3s",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity3> create(@Valid @RequestBody TestEntity3 testEntity3) throws URISyntaxException {
        log.debug("REST request to save TestEntity3 : {}", testEntity3);
        if (testEntity3.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new testEntity3 cannot already have an ID").body(null);
        }
        TestEntity3 result = testEntity3Repository.save(testEntity3);
        testEntity3SearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/testEntity3s/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("testEntity3", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /testEntity3s -> Updates an existing testEntity3.
     */
    @RequestMapping(value = "/testEntity3s",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity3> update(@Valid @RequestBody TestEntity3 testEntity3) throws URISyntaxException {
        log.debug("REST request to update TestEntity3 : {}", testEntity3);
        if (testEntity3.getId() == null) {
            return create(testEntity3);
        }
        TestEntity3 result = testEntity3Repository.save(testEntity3);
        testEntity3SearchRepository.save(testEntity3);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("testEntity3", testEntity3.getId().toString()))
                .body(result);
    }

    /**
     * GET  /testEntity3s -> get all the testEntity3s.
     */
    @RequestMapping(value = "/testEntity3s",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TestEntity3>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<TestEntity3> page = testEntity3Repository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/testEntity3s", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /testEntity3s/:id -> get the "id" testEntity3.
     */
    @RequestMapping(value = "/testEntity3s/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity3> get(@PathVariable Long id) {
        log.debug("REST request to get TestEntity3 : {}", id);
        return Optional.ofNullable(testEntity3Repository.findOneWithEagerRelationships(id))
            .map(testEntity3 -> new ResponseEntity<>(
                testEntity3,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testEntity3s/:id -> delete the "id" testEntity3.
     */
    @RequestMapping(value = "/testEntity3s/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete TestEntity3 : {}", id);
        testEntity3Repository.delete(id);
        testEntity3SearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testEntity3", id.toString())).build();
    }

    /**
     * SEARCH  /_search/testEntity3s/:query -> search for the testEntity3 corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/testEntity3s/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TestEntity3> search(@PathVariable String query) {
        return StreamSupport
            .stream(testEntity3SearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
