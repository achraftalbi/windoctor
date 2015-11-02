package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Treatment;
import com.winbit.windoctor.repository.TreatmentRepository;
import com.winbit.windoctor.repository.search.TreatmentSearchRepository;
import com.winbit.windoctor.web.rest.util.HeaderUtil;
import com.winbit.windoctor.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.winbit.windoctor.web.rest.util.FunctionsUtil.addDays;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Treatment.
 */
@RestController
@RequestMapping("/api")
public class TreatmentResource {

    private final Logger log = LoggerFactory.getLogger(TreatmentResource.class);

    @Inject
    private TreatmentRepository treatmentRepository;

    @Inject
    private TreatmentSearchRepository treatmentSearchRepository;

    /**
     * POST  /treatments -> Create a new treatment.
     */
    @RequestMapping(value = "/treatments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Treatment> create(@Valid @RequestBody Treatment treatment) throws URISyntaxException {
        log.debug("REST request to save Treatment : {}", treatment);
        if (treatment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new treatment cannot already have an ID").body(null);
        }
        Treatment result = treatmentRepository.save(treatment);
        treatmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/treatments/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("treatment", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /treatments -> Updates an existing treatment.
     */
    @RequestMapping(value = "/treatments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Treatment> update(@Valid @RequestBody Treatment treatment) throws URISyntaxException {
        log.debug("REST request to update Treatment : {}", treatment);
        if (treatment.getId() == null) {
            return create(treatment);
        }
        Treatment result = treatmentRepository.save(treatment);
        treatmentSearchRepository.save(treatment);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("treatment", treatment.getId().toString()))
                .body(result);
    }

    /**
     * GET  /treatments -> get all the treatments.
     */
    @RequestMapping(value = "/treatments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Treatment>> getAll(@RequestParam(value = "eventId" , required = false) Long eventId, @RequestParam(value = "patientId" , required = false) Long patientId,@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Treatment> page = null;
        if (patientId!=null){
            log.debug("patientId part -> eventId:"+eventId+" patientId:"+patientId);
            page = treatmentRepository.findByPatient(patientId, PaginationUtil.generatePageRequest(offset, limit));
        }else if (eventId!=null){
            log.debug("eventId part -> eventId:"+eventId+" patientId:"+patientId);
            page = treatmentRepository.findByEvent(eventId, PaginationUtil.generatePageRequest(offset, limit));
        }else{
            log.debug("last part -> eventId:"+eventId+" patientId:"+patientId);
            page = treatmentRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/treatments", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /treatments/:id -> get the "id" treatment.
     */
    @RequestMapping(value = "/treatments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Treatment> get(@PathVariable Long id) {
        log.debug("REST request to get Treatment : {}", id);
        return Optional.ofNullable(treatmentRepository.findOne(id))
            .map(treatment -> new ResponseEntity<>(
                treatment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /treatments/:id -> delete the "id" treatment.
     */
    @RequestMapping(value = "/treatments/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Treatment : {}", id);
        treatmentRepository.delete(id);
        treatmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("treatment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/treatments/:query -> search for the treatment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/treatments/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Treatment> search(@PathVariable String query) {
        return StreamSupport
            .stream(treatmentSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
