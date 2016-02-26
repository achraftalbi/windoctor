package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.SecurityConfiguration;
import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.repository.FundRepository;
import com.winbit.windoctor.repository.Fund_historyRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.FundSearchRepository;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.web.rest.dto.FundDTO;
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
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Fund.
 */
@RestController
@RequestMapping("/api")
public class FundResource {

    private final Logger log = LoggerFactory.getLogger(FundResource.class);

    @Inject
    private FundRepository fundRepository;

    @Inject
    private Fund_historyRepository fund_historyRepository;

    @Inject
    private FundSearchRepository fundSearchRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /funds -> Create a new fund.
     */
    @RequestMapping(value = "/funds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fund> create(@Valid @RequestBody FundDTO fund) throws URISyntaxException {
        log.debug("REST request to save Fund : {}", fund);
        if (fund.getFund().getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new fund cannot already have an ID").body(null);
        }
        BigDecimal oldFundAmount;
        oldFundAmount = fund.getFund().getId()==null?new BigDecimal(0l):fundRepository.findOne(fund.getFund().getId()).getAmount();
        fund.getFund().setStructure(new Structure());
        fund.getFund().getStructure().setId(SecurityUtils.getCurrerntStructure());
        Fund result = fundRepository.save(fund.getFund());
        fundSearchRepository.save(result);
        saveFundHistory(fund,oldFundAmount);
        return ResponseEntity.created(new URI("/api/funds/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("fund", result.getDescription().toString()))
                .body(result);
    }

    /**
     * PUT  /funds -> Updates an existing fund.
     */
    @RequestMapping(value = "/funds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fund> update(@Valid @RequestBody FundDTO fund) throws URISyntaxException {
        log.debug("REST request to update Fund : {}", fund);
        if (fund.getFund().getId() == null) {
            return create(fund);
        }
        log.debug("fund.getSupply_type().getDescription() " + fund.getSupply_type().getDescription());
        BigDecimal oldFundAmount = fundRepository.findOne(fund.getFund().getId()).getAmount();
        Fund result = fundRepository.save(fund.getFund());
        fundSearchRepository.save(fund.getFund());
        saveFundHistory(fund,oldFundAmount);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("fund", fund.getFund().getDescription().toString()))
                .body(result);
    }

    /**
     * GET  /funds -> get all the funds.
     */
    @RequestMapping(value = "/funds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fund>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Fund> page = fundRepository.findAll(SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/funds", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /funds/:id -> get the "id" fund.
     */
    @RequestMapping(value = "/funds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fund> get(@PathVariable Long id) {
        log.debug("REST request to get Fund : {}", id);
        return Optional.ofNullable(fundRepository.findOne(id))
            .map(fund -> new ResponseEntity<>(
                fund,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /funds/:id -> delete the "id" fund.
     */
    @RequestMapping(value = "/funds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Fund : {}", id);
        fundRepository.delete(id);
        fundSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fund", id.toString())).build();
    }

    /**
     * SEARCH  /_search/funds/:query -> search for the fund corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/funds/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fund> search(@PathVariable String query) {
        return StreamSupport
            .stream(fundSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public void saveFundHistory(FundDTO fund, BigDecimal oldFundAmount){
        Fund_history fund_history = new Fund_history();
        fund_history.setFund(fund.getFund());
        fund_history.setNew_amount(fund.getFund().getAmount());
        fund_history.setOld_amount(oldFundAmount);
        fund_history.setAmount_movement(new BigDecimal(fund_history.getNew_amount().doubleValue() - fund_history.getOld_amount().doubleValue()));
        fund_history.setType_operation(fund_history.getAmount_movement().doubleValue() >= 0d ? true : false);
        fund_history.setSupply_type(fund.getSupply_type());
        Optional<User>  user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        fund_history.setCreated_by(user.isPresent() ? user.get() : null);
        fund_history.setCreation_date(new DateTime());
        fund_historyRepository.save(fund_history);
    }


}
