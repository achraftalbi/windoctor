package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Medication;
import com.winbit.windoctor.repository.MedicationRepository;
import com.winbit.windoctor.repository.search.MedicationSearchRepository;
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
 * REST controller for managing Medication.
 */
@RestController
@RequestMapping("/api")
public class MedicationResource {

    private final Logger log = LoggerFactory.getLogger(MedicationResource.class);

    @Inject
    private MedicationRepository medicationRepository;

    @Inject
    private MedicationSearchRepository medicationSearchRepository;

    /**
     * POST  /medications -> Create a new medication.
     */
    @RequestMapping(value = "/medications",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medication> create(@Valid @RequestBody Medication medication) throws URISyntaxException {
        log.debug("REST request to save Medication : {}", medication);
        if (medication.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new medication cannot already have an ID").body(null);
        }
        Medication result = medicationRepository.save(medication);
        medicationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medications/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("medication", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /medications -> Updates an existing medication.
     */
    @RequestMapping(value = "/medications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medication> update(@Valid @RequestBody Medication medication) throws URISyntaxException {
        log.debug("REST request to update Medication : {}", medication);
        if (medication.getId() == null) {
            return create(medication);
        }
        Medication result = medicationRepository.save(medication);
        medicationSearchRepository.save(medication);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("medication", medication.getId().toString()))
                .body(result);
    }

    /**
     * GET  /medications -> get all the medications.
     */
    @RequestMapping(value = "/medications",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Medication>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Medication> page = medicationRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medications", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medications/:id -> get the "id" medication.
     */
    @RequestMapping(value = "/medications/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Medication> get(@PathVariable Long id) {
        log.debug("REST request to get Medication : {}", id);
        return Optional.ofNullable(medicationRepository.findOne(id))
            .map(medication -> new ResponseEntity<>(
                medication,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /medications/:id -> delete the "id" medication.
     */
    @RequestMapping(value = "/medications/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Medication : {}", id);
        medicationRepository.delete(id);
        medicationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("medication", id.toString())).build();
    }

    /**
     * SEARCH  /_search/medications/:query -> search for the medication corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/medications/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Medication> search(@PathVariable String query) {
        return StreamSupport
            .stream(medicationSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
