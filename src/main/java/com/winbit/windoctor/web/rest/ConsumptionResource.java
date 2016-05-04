package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.Consumption;
import com.winbit.windoctor.repository.ConsumptionRepository;
import com.winbit.windoctor.repository.search.ConsumptionSearchRepository;
import com.winbit.windoctor.web.rest.util.FunctionsUtil;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Consumption.
 */
@RestController
@RequestMapping("/api")
public class ConsumptionResource {

    private final Logger log = LoggerFactory.getLogger(ConsumptionResource.class);

    @Inject
    private ConsumptionRepository consumptionRepository;

    @Inject
    private ConsumptionSearchRepository consumptionSearchRepository;

    /**
     * POST  /consumptions -> Create a new consumption.
     */
    @RequestMapping(value = "/consumptions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Consumption> create(@Valid @RequestBody Consumption consumption) throws URISyntaxException {
        log.debug("REST request to save Consumption : {}", consumption);
        if (consumption.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new consumption cannot already have an ID").body(null);
        }
        consumption.setCreation_date(new DateTime());
        Consumption result = consumptionRepository.save(consumption);
        consumptionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/consumptions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("consumption", result.getCreation_date() == null ? "" :
                    FunctionsUtil.convertDateToString(result.getCreation_date().toDate(), Constants.GLOBAL_DATE_FORMAT)))
                .body(result);
    }

    /**
     * PUT  /consumptions -> Updates an existing consumption.
     */
    @RequestMapping(value = "/consumptions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Consumption> update(@Valid @RequestBody Consumption consumption) throws URISyntaxException {
        log.debug("REST request to update Consumption : {}", consumption);
        if (consumption.getId() == null) {
            return create(consumption);
        }
        Consumption result = consumptionRepository.save(consumption);
        consumptionSearchRepository.save(consumption);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("consumption", result.getCreation_date() == null ? "" :
                    FunctionsUtil.convertDateToString(result.getCreation_date().toDate(), Constants.GLOBAL_DATE_FORMAT)))
                .body(result);
    }

    /**
     * GET  /consumptions -> get all the consumptions.
     */
    @RequestMapping(value = "/consumptions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Consumption>> getAll(@RequestParam(value = "productId" , required = false) Long productId,
                                                    @RequestParam(value = "page" , required = false) Integer offset,
                                                    @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Consumption> page = consumptionRepository.findAll(productId,PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/consumptions", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /consumptions/:id -> get the "id" consumption.
     */
    @RequestMapping(value = "/consumptions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Consumption> get(@PathVariable Long id) {
        log.debug("REST request to get Consumption : {}", id);
        return Optional.ofNullable(consumptionRepository.findOne(id))
            .map(consumption -> new ResponseEntity<>(
                consumption,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /consumptions/:id -> delete the "id" consumption.
     */
    @RequestMapping(value = "/consumptions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Consumption : {}", id);
        Consumption consumption = consumptionRepository.findOne(id);
        consumptionRepository.delete(id);
        consumptionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("consumption", consumption.getCreation_date() == null ? "" : FunctionsUtil.convertDateToString(consumption.getCreation_date().toDate(), Constants.GLOBAL_DATE_FORMAT))).build();
    }

    /**
     * SEARCH  /_search/consumptions/:query -> search for the consumption corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/consumptions/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Consumption> search(@PathVariable String query) {
        return StreamSupport
            .stream(consumptionSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
