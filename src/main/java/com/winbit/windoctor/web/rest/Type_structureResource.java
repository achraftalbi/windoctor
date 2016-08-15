package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Type_structure;
import com.winbit.windoctor.repository.Type_structureRepository;
import com.winbit.windoctor.repository.search.Type_structureSearchRepository;
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
 * REST controller for managing Type_structure.
 */
@RestController
@RequestMapping("/api")
public class Type_structureResource {

    private final Logger log = LoggerFactory.getLogger(Type_structureResource.class);

    @Inject
    private Type_structureRepository type_structureRepository;

    @Inject
    private Type_structureSearchRepository type_structureSearchRepository;

    /**
     * POST  /type_structures -> Create a new type_structure.
     */
    @RequestMapping(value = "/type_structures",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Type_structure> create(@Valid @RequestBody Type_structure type_structure) throws URISyntaxException {
        log.debug("REST request to save Type_structure : {}", type_structure);
        if (type_structure.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new type_structure cannot already have an ID").body(null);
        }
        Type_structure result = type_structureRepository.save(type_structure);
        type_structureSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type_structures/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("type_structure", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /type_structures -> Updates an existing type_structure.
     */
    @RequestMapping(value = "/type_structures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Type_structure> update(@Valid @RequestBody Type_structure type_structure) throws URISyntaxException {
        log.debug("REST request to update Type_structure : {}", type_structure);
        if (type_structure.getId() == null) {
            return create(type_structure);
        }
        Type_structure result = type_structureRepository.save(type_structure);
        type_structureSearchRepository.save(type_structure);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("type_structure", type_structure.getId().toString()))
                .body(result);
    }

    /**
     * GET  /type_structures -> get all the type_structures.
     */
    @RequestMapping(value = "/type_structures",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Type_structure>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Type_structure> page = type_structureRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/type_structures", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /type_structures/:id -> get the "id" type_structure.
     */
    @RequestMapping(value = "/type_structures/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Type_structure> get(@PathVariable Long id) {
        log.debug("REST request to get Type_structure : {}", id);
        return Optional.ofNullable(type_structureRepository.findOne(id))
            .map(type_structure -> new ResponseEntity<>(
                type_structure,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type_structures/:id -> delete the "id" type_structure.
     */
    @RequestMapping(value = "/type_structures/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Type_structure : {}", id);
        type_structureRepository.delete(id);
        type_structureSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("type_structure", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type_structures/:query -> search for the type_structure corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/type_structures/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Type_structure> search(@PathVariable String query) {
        return StreamSupport
            .stream(type_structureSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
