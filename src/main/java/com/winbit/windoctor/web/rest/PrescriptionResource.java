package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Prescription;
import com.winbit.windoctor.repository.PrescriptionRepository;
import com.winbit.windoctor.repository.search.PrescriptionSearchRepository;
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
 * REST controller for managing Prescription.
 */
@RestController
@RequestMapping("/api")
public class PrescriptionResource {

    private final Logger log = LoggerFactory.getLogger(PrescriptionResource.class);

    @Inject
    private PrescriptionRepository prescriptionRepository;

    @Inject
    private PrescriptionSearchRepository prescriptionSearchRepository;

    /**
     * POST  /prescriptions -> Create a new prescription.
     */
    @RequestMapping(value = "/prescriptions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prescription> create(@Valid @RequestBody Prescription prescription) throws URISyntaxException {
        log.debug("REST request to save Prescription : {}", prescription);
        if (prescription.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new prescription cannot already have an ID").body(null);
        }
        Prescription result = prescriptionRepository.save(prescription);
        prescriptionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/prescriptions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("prescription", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /prescriptions -> Updates an existing prescription.
     */
    @RequestMapping(value = "/prescriptions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prescription> update(@Valid @RequestBody Prescription prescription) throws URISyntaxException {
        log.debug("REST request to update Prescription : {}", prescription);
        if (prescription.getId() == null) {
            return create(prescription);
        }
        Prescription result = prescriptionRepository.save(prescription);
        prescriptionSearchRepository.save(prescription);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("prescription", prescription.getId().toString()))
                .body(result);
    }

    /**
     * GET  /prescriptions -> get all the prescriptions.
     */
    @RequestMapping(value = "/prescriptions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Prescription>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Prescription> page = prescriptionRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prescriptions", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /prescriptions/:id -> get the "id" prescription.
     */
    @RequestMapping(value = "/prescriptions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prescription> get(@PathVariable Long id) {
        log.debug("REST request to get Prescription : {}", id);
        return Optional.ofNullable(prescriptionRepository.findOne(id))
            .map(prescription -> new ResponseEntity<>(
                prescription,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prescriptions/:id -> delete the "id" prescription.
     */
    @RequestMapping(value = "/prescriptions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Prescription : {}", id);
        prescriptionRepository.delete(id);
        prescriptionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prescription", id.toString())).build();
    }

    /**
     * SEARCH  /_search/prescriptions/:query -> search for the prescription corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/prescriptions/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Prescription> search(@PathVariable String query) {
        return StreamSupport
            .stream(prescriptionSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
