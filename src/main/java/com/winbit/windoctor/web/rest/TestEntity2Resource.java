package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.TestEntity2;
import com.winbit.windoctor.repository.TestEntity2Repository;
import com.winbit.windoctor.repository.search.TestEntity2SearchRepository;
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
 * REST controller for managing TestEntity2.
 */
@RestController
@RequestMapping("/api")
public class TestEntity2Resource {

    private final Logger log = LoggerFactory.getLogger(TestEntity2Resource.class);

    @Inject
    private TestEntity2Repository testEntity2Repository;

    @Inject
    private TestEntity2SearchRepository testEntity2SearchRepository;

    /**
     * POST  /testEntity2s -> Create a new testEntity2.
     */
    @RequestMapping(value = "/testEntity2s",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity2> create(@Valid @RequestBody TestEntity2 testEntity2) throws URISyntaxException {
        log.debug("REST request to save TestEntity2 : {}", testEntity2);
        if (testEntity2.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new testEntity2 cannot already have an ID").body(null);
        }
        TestEntity2 result = testEntity2Repository.save(testEntity2);
        testEntity2SearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/testEntity2s/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("testEntity2", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /testEntity2s -> Updates an existing testEntity2.
     */
    @RequestMapping(value = "/testEntity2s",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity2> update(@Valid @RequestBody TestEntity2 testEntity2) throws URISyntaxException {
        log.debug("REST request to update TestEntity2 : {}", testEntity2);
        if (testEntity2.getId() == null) {
            return create(testEntity2);
        }
        TestEntity2 result = testEntity2Repository.save(testEntity2);
        testEntity2SearchRepository.save(testEntity2);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("testEntity2", testEntity2.getId().toString()))
                .body(result);
    }

    /**
     * GET  /testEntity2s -> get all the testEntity2s.
     */
    @RequestMapping(value = "/testEntity2s",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TestEntity2>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<TestEntity2> page = testEntity2Repository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/testEntity2s", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /testEntity2s/:id -> get the "id" testEntity2.
     */
    @RequestMapping(value = "/testEntity2s/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity2> get(@PathVariable Long id) {
        log.debug("REST request to get TestEntity2 : {}", id);
        return Optional.ofNullable(testEntity2Repository.findOne(id))
            .map(testEntity2 -> new ResponseEntity<>(
                testEntity2,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testEntity2s/:id -> delete the "id" testEntity2.
     */
    @RequestMapping(value = "/testEntity2s/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete TestEntity2 : {}", id);
        testEntity2Repository.delete(id);
        testEntity2SearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testEntity2", id.toString())).build();
    }

    /**
     * SEARCH  /_search/testEntity2s/:query -> search for the testEntity2 corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/testEntity2s/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TestEntity2> search(@PathVariable String query) {
        return StreamSupport
            .stream(testEntity2SearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
