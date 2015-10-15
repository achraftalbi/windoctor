package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.TestEntity5;
import com.winbit.windoctor.repository.TestEntity5Repository;
import com.winbit.windoctor.repository.search.TestEntity5SearchRepository;
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
 * REST controller for managing TestEntity5.
 */
@RestController
@RequestMapping("/api")
public class TestEntity5Resource {

    private final Logger log = LoggerFactory.getLogger(TestEntity5Resource.class);

    @Inject
    private TestEntity5Repository testEntity5Repository;

    @Inject
    private TestEntity5SearchRepository testEntity5SearchRepository;

    /**
     * POST  /testEntity5s -> Create a new testEntity5.
     */
    @RequestMapping(value = "/testEntity5s",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity5> create(@RequestBody TestEntity5 testEntity5) throws URISyntaxException {
        log.debug("REST request to save TestEntity5 : {}", testEntity5);
        if (testEntity5.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new testEntity5 cannot already have an ID").body(null);
        }
        TestEntity5 result = testEntity5Repository.save(testEntity5);
        testEntity5SearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/testEntity5s/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("testEntity5", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /testEntity5s -> Updates an existing testEntity5.
     */
    @RequestMapping(value = "/testEntity5s",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity5> update(@RequestBody TestEntity5 testEntity5) throws URISyntaxException {
        log.debug("REST request to update TestEntity5 : {}", testEntity5);
        if (testEntity5.getId() == null) {
            return create(testEntity5);
        }
        TestEntity5 result = testEntity5Repository.save(testEntity5);
        testEntity5SearchRepository.save(testEntity5);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("testEntity5", testEntity5.getId().toString()))
                .body(result);
    }

    /**
     * GET  /testEntity5s -> get all the testEntity5s.
     */
    @RequestMapping(value = "/testEntity5s",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TestEntity5>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<TestEntity5> page = testEntity5Repository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/testEntity5s", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /testEntity5s/:id -> get the "id" testEntity5.
     */
    @RequestMapping(value = "/testEntity5s/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity5> get(@PathVariable Long id) {
        log.debug("REST request to get TestEntity5 : {}", id);
        return Optional.ofNullable(testEntity5Repository.findOne(id))
            .map(testEntity5 -> new ResponseEntity<>(
                testEntity5,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testEntity5s/:id -> delete the "id" testEntity5.
     */
    @RequestMapping(value = "/testEntity5s/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete TestEntity5 : {}", id);
        testEntity5Repository.delete(id);
        testEntity5SearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testEntity5", id.toString())).build();
    }

    /**
     * SEARCH  /_search/testEntity5s/:query -> search for the testEntity5 corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/testEntity5s/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TestEntity5> search(@PathVariable String query) {
        return StreamSupport
            .stream(testEntity5SearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
