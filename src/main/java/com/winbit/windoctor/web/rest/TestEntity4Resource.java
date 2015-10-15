package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.TestEntity4;
import com.winbit.windoctor.repository.TestEntity4Repository;
import com.winbit.windoctor.repository.search.TestEntity4SearchRepository;
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
 * REST controller for managing TestEntity4.
 */
@RestController
@RequestMapping("/api")
public class TestEntity4Resource {

    private final Logger log = LoggerFactory.getLogger(TestEntity4Resource.class);

    @Inject
    private TestEntity4Repository testEntity4Repository;

    @Inject
    private TestEntity4SearchRepository testEntity4SearchRepository;

    /**
     * POST  /testEntity4s -> Create a new testEntity4.
     */
    @RequestMapping(value = "/testEntity4s",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity4> create(@RequestBody TestEntity4 testEntity4) throws URISyntaxException {
        log.debug("REST request to save TestEntity4 : {}", testEntity4);
        if (testEntity4.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new testEntity4 cannot already have an ID").body(null);
        }
        TestEntity4 result = testEntity4Repository.save(testEntity4);
        testEntity4SearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/testEntity4s/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("testEntity4", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /testEntity4s -> Updates an existing testEntity4.
     */
    @RequestMapping(value = "/testEntity4s",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity4> update(@RequestBody TestEntity4 testEntity4) throws URISyntaxException {
        log.debug("REST request to update TestEntity4 : {}", testEntity4);
        if (testEntity4.getId() == null) {
            return create(testEntity4);
        }
        TestEntity4 result = testEntity4Repository.save(testEntity4);
        testEntity4SearchRepository.save(testEntity4);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("testEntity4", testEntity4.getId().toString()))
                .body(result);
    }

    /**
     * GET  /testEntity4s -> get all the testEntity4s.
     */
    @RequestMapping(value = "/testEntity4s",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TestEntity4>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("testentity5-is-null".equals(filter)) {
            log.debug("REST request to get all TestEntity4s where testEntity5 is null");
            return new ResponseEntity<>(StreamSupport
                .stream(testEntity4Repository.findAll().spliterator(), false)
                .filter(testEntity4 -> testEntity4.getTestEntity5() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        
        Page<TestEntity4> page = testEntity4Repository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/testEntity4s", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /testEntity4s/:id -> get the "id" testEntity4.
     */
    @RequestMapping(value = "/testEntity4s/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestEntity4> get(@PathVariable Long id) {
        log.debug("REST request to get TestEntity4 : {}", id);
        return Optional.ofNullable(testEntity4Repository.findOne(id))
            .map(testEntity4 -> new ResponseEntity<>(
                testEntity4,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testEntity4s/:id -> delete the "id" testEntity4.
     */
    @RequestMapping(value = "/testEntity4s/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete TestEntity4 : {}", id);
        testEntity4Repository.delete(id);
        testEntity4SearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testEntity4", id.toString())).build();
    }

    /**
     * SEARCH  /_search/testEntity4s/:query -> search for the testEntity4 corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/testEntity4s/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TestEntity4> search(@PathVariable String query) {
        return StreamSupport
            .stream(testEntity4SearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
