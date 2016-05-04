package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.repository.*;
import com.winbit.windoctor.repository.search.ChargeSearchRepository;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import com.winbit.windoctor.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;


/**
 * REST controller for managing Charge.
 */
@RestController
@RequestMapping("/api")
public class ChargeResource {

    private final Logger log = LoggerFactory.getLogger(ChargeResource.class);

    @Inject
    private ChargeRepository chargeRepository;

    @Inject
    private ChargeSearchRepository chargeSearchRepository;

    @Inject
    private EntryRepository entryRepository;


    /**
     * POST  /charges -> Create a new charge.
     */
    @RequestMapping(value = "/charges",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Charge> create(@Valid @RequestBody Charge charge) throws URISyntaxException {
        log.debug("REST request to save Charge : {}", charge);
        if (charge.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new charge cannot already have an ID").body(null);
        }
        charge.setStructure(new Structure());
        charge.getStructure().setId(SecurityUtils.getCurrerntStructure());
        // Decrease amount from the current fund.

        Charge result = chargeRepository.save(charge);
        chargeSearchRepository.save(result);


        return ResponseEntity.created(new URI("/api/charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("charge", result.getName().toString()))
            .body(result);
    }

    /**
     * PUT  /charges -> Updates an existing charge.
     */
    @RequestMapping(value = "/charges",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Charge> update(@Valid @RequestBody Charge charge) throws URISyntaxException {
        log.debug("REST request to update Charge : {}", charge);
        if (charge.getId() == null) {
            return create(charge);
        }

        Charge result = chargeRepository.save(charge);
        chargeSearchRepository.save(charge);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("charge", charge.getName().toString()))
            .body(result);
    }

    /**
     * GET  /charges -> get all the charges.
     */
    @RequestMapping(value = "/charges",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Charge>> getAll(@RequestParam(value = "typeChargeToGet", required = false) Long typeChargeToGet,
                                                @RequestParam(value = "page", required = false) Integer offset,
                                                @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get charges page per_page");
        Page<Charge> page;
        page = chargeRepository.findAll(SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
        List<Charge> charges = page.getContent();
        for (Charge charge : charges) {
            Entry entry = entryRepository.findTotalCharge(charge.getId());
            charge.setPrice(entry.getPrice());
            charge.setAmount(new BigDecimal(entry.getAmount().doubleValue()));
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/charges", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /charges/:id -> get the "id" charge.
     */
    @RequestMapping(value = "/charges/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Charge> get(@PathVariable Long id) {
        log.debug("REST request to get Charge : {}", id);
        Charge chargeTmp =chargeRepository.findOne(id);
        Entry entry = entryRepository.findTotalCharge(chargeTmp.getId());
        chargeTmp.setPrice(entry.getPrice());
        chargeTmp.setAmount(entry.getAmount());

        return Optional.ofNullable(chargeTmp)
            .map(charge -> new ResponseEntity<>(
                charge,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /charges/:id -> delete the "id" charge.
     */
    @RequestMapping(value = "/charges/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Charge : {}", id);
        chargeRepository.delete(id);
        chargeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("charge", id.toString())).build();
    }

    /**
     * SEARCH  /_search/charges/:query -> search for the charge corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/charges/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    /*public List<Charge> search(@PathVariable String query) {
        return StreamSupport
            .stream(chargeSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }*/
    public ResponseEntity<List<Charge>> search(@PathVariable String query, @RequestParam(value = "page", required = false) Integer offset,
                                                @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Charge> page;
        page = chargeRepository.findAllMatchString(Constants.PERCENTAGE + query + Constants.PERCENTAGE, SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
        List<Charge> charges = page.getContent();
        for (Charge charge : charges) {
            Entry entry = entryRepository.findTotalCharge(charge.getId());
            charge.setPrice(entry.getPrice());
            charge.setAmount(entry.getAmount());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/charges", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
