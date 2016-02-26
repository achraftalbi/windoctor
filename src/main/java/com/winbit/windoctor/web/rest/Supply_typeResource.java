package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.domain.Supply_type;
import com.winbit.windoctor.repository.Supply_typeRepository;
import com.winbit.windoctor.repository.search.Supply_typeSearchRepository;
import com.winbit.windoctor.security.SecurityUtils;
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
 * REST controller for managing Supply_type.
 */
@RestController
@RequestMapping("/api")
public class Supply_typeResource {

    private final Logger log = LoggerFactory.getLogger(Supply_typeResource.class);

    @Inject
    private Supply_typeRepository supply_typeRepository;

    @Inject
    private Supply_typeSearchRepository supply_typeSearchRepository;

    /**
     * POST  /supply_types -> Create a new supply_type.
     */
    @RequestMapping(value = "/supply_types",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Supply_type> create(@Valid @RequestBody Supply_type supply_type) throws URISyntaxException {
        log.debug("REST request to save Supply_type : {}", supply_type);
        if (supply_type.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new supply_type cannot already have an ID").body(null);
        }
        supply_type.setStructure(new Structure());
        supply_type.getStructure().setId(SecurityUtils.getCurrerntStructure());
        Supply_type result = supply_typeRepository.save(supply_type);
        supply_typeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/supply_types/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("supply_type", result.getDescription().toString()))
                .body(result);
    }

    /**
     * PUT  /supply_types -> Updates an existing supply_type.
     */
    @RequestMapping(value = "/supply_types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Supply_type> update(@Valid @RequestBody Supply_type supply_type) throws URISyntaxException {
        log.debug("REST request to update Supply_type : {}", supply_type);
        if (supply_type.getId() == null) {
            return create(supply_type);
        }
        Supply_type result = supply_typeRepository.save(supply_type);
        supply_typeSearchRepository.save(supply_type);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("supply_type", supply_type.getDescription().toString()))
                .body(result);
    }

    /**
     * GET  /supply_types -> get all the supply_types.
     */
    @RequestMapping(value = "/supply_types",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Supply_type>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Supply_type> page = supply_typeRepository.findAll(SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/supply_types", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /supply_types/:id -> get the "id" supply_type.
     */
    @RequestMapping(value = "/supply_types/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Supply_type> get(@PathVariable Long id) {
        log.debug("REST request to get Supply_type : {}", id);
        return Optional.ofNullable(supply_typeRepository.findOne(id))
            .map(supply_type -> new ResponseEntity<>(
                supply_type,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /supply_types/:id -> delete the "id" supply_type.
     */
    @RequestMapping(value = "/supply_types/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Supply_type : {}", id);
        supply_typeRepository.delete(id);
        supply_typeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("supply_type", id.toString())).build();
    }

    /**
     * SEARCH  /_search/supply_types/:query -> search for the supply_type corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/supply_types/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Supply_type> search(@PathVariable String query) {
        return StreamSupport
            .stream(supply_typeSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
