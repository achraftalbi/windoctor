package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Prescription_medication;
import com.winbit.windoctor.repository.Prescription_medicationRepository;
import com.winbit.windoctor.repository.search.Prescription_medicationSearchRepository;
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
 * REST controller for managing Prescription_medication.
 */
@RestController
@RequestMapping("/api")
public class Prescription_medicationResource {

    private final Logger log = LoggerFactory.getLogger(Prescription_medicationResource.class);

    @Inject
    private Prescription_medicationRepository prescription_medicationRepository;

    @Inject
    private Prescription_medicationSearchRepository prescription_medicationSearchRepository;

    /**
     * POST  /prescription_medications -> Create a new prescription_medication.
     */
    @RequestMapping(value = "/prescription_medications",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prescription_medication> create(@Valid @RequestBody Prescription_medication prescription_medication) throws URISyntaxException {
        log.debug("REST request to save Prescription_medication : {}", prescription_medication);
        if (prescription_medication.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new prescription_medication cannot already have an ID").body(null);
        }
        Prescription_medication result = prescription_medicationRepository.save(prescription_medication);
        prescription_medicationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/prescription_medications/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("prescription_medication", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /prescription_medications -> Updates an existing prescription_medication.
     */
    @RequestMapping(value = "/prescription_medications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prescription_medication> update(@Valid @RequestBody Prescription_medication prescription_medication) throws URISyntaxException {
        log.debug("REST request to update Prescription_medication : {}", prescription_medication);
        if (prescription_medication.getId() == null) {
            return create(prescription_medication);
        }
        Prescription_medication result = prescription_medicationRepository.save(prescription_medication);
        prescription_medicationSearchRepository.save(prescription_medication);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("prescription_medication", prescription_medication.getId().toString()))
                .body(result);
    }

    /**
     * GET  /prescription_medications -> get all the prescription_medications.
     */
    @RequestMapping(value = "/prescription_medications",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Prescription_medication>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Prescription_medication> page = prescription_medicationRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prescription_medications", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prescription_medications/:id -> get the "id" prescription_medication.
     */
    @RequestMapping(value = "/prescription_medications/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prescription_medication> get(@PathVariable Long id) {
        log.debug("REST request to get Prescription_medication : {}", id);
        return Optional.ofNullable(prescription_medicationRepository.findOne(id))
            .map(prescription_medication -> new ResponseEntity<>(
                prescription_medication,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prescription_medications/:id -> delete the "id" prescription_medication.
     */
    @RequestMapping(value = "/prescription_medications/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Prescription_medication : {}", id);
        prescription_medicationRepository.delete(id);
        prescription_medicationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prescription_medication", id.toString())).build();
    }

    /**
     * SEARCH  /_search/prescription_medications/:query -> search for the prescription_medication corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/prescription_medications/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Prescription_medication> search(@PathVariable String query) {
        return StreamSupport
            .stream(prescription_medicationSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
