package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.repository.StructureRepository;
import com.winbit.windoctor.repository.search.StructureSearchRepository;
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
 * REST controller for managing Structure.
 */
@RestController
@RequestMapping("/api")
public class StructureResource {

    private final Logger log = LoggerFactory.getLogger(StructureResource.class);

    @Inject
    private StructureRepository structureRepository;

    @Inject
    private StructureSearchRepository structureSearchRepository;

    /**
     * POST  /structures -> Create a new structure.
     */
    @RequestMapping(value = "/structures",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Structure> create(@RequestBody Structure structure) throws URISyntaxException {
        log.debug("REST request to save Structure : {}", structure);
        if (structure.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new structure cannot already have an ID").body(null);
        }
        Structure result = structureRepository.save(structure);
        structureSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/structures/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("structure", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /structures -> Updates an existing structure.
     */
    @RequestMapping(value = "/structures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Structure> update(@RequestBody Structure structure) throws URISyntaxException {
        log.debug("REST request to update Structure : {}", structure);
        if (structure.getId() == null) {
            return create(structure);
        }
        Structure result = structureRepository.save(structure);
        structureSearchRepository.save(structure);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("structure", structure.getId().toString()))
                .body(result);
    }

    /**
     * GET  /structures -> get all the structures.
     */
    @RequestMapping(value = "/structures",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Structure>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Structure> page = structureRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/structures", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /structures/:id -> get the "id" structure.
     */
    @RequestMapping(value = "/structures/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Structure> get(@PathVariable Long id) {
        log.debug("REST request to get Structure : {}", id);
        return Optional.ofNullable(structureRepository.findOne(id))
            .map(structure -> new ResponseEntity<>(
                structure,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /structures/:id -> delete the "id" structure.
     */
    @RequestMapping(value = "/structures/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Structure : {}", id);
        structureRepository.delete(id);
        structureSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("structure", id.toString())).build();
    }

    /**
     * SEARCH  /_search/structures/:query -> search for the structure corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/structures/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Structure> search(@PathVariable String query) {
        return StreamSupport
            .stream(structureSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
