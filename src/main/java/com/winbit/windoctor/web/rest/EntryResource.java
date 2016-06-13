package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.repository.FundRepository;
import com.winbit.windoctor.repository.Fund_historyRepository;
import com.winbit.windoctor.repository.EntryRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.FundSearchRepository;
import com.winbit.windoctor.repository.search.EntrySearchRepository;
import com.winbit.windoctor.security.SecurityUtils;
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
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Entry.
 */
@RestController
@RequestMapping("/api")
public class EntryResource {

    private final Logger log = LoggerFactory.getLogger(EntryResource.class);

    @Inject
    private EntryRepository entryRepository;

    @Inject
    private EntrySearchRepository entrySearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Fund_historyRepository fund_historyRepository;

    @Inject
    private FundRepository fundRepository;

    @Inject
    private FundSearchRepository fundSearchRepository;

    /**
     * POST  /entrys -> Create a new entry.
     */
    @RequestMapping(value = "/entrys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Entry> create(@Valid @RequestBody Entry entry) throws URISyntaxException {
        log.debug("REST request to save Entry : {}", entry);
        if (entry.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new entry cannot already have an ID").body(null);
        }

        if(entry.getFund()!=null && entry.getFund().getId()!=null){
            entry.setFund(fundRepository.findOne(entry.getFund().getId()));
        }

        BigDecimal oldFundAmount = new BigDecimal(entry.getFund().getAmount().doubleValue());
        entry.getFund().setAmount(new BigDecimal(entry.getFund().getAmount().doubleValue() - (entry.getAmount().doubleValue()*entry.getPrice().doubleValue())));
        entry.setFund(fundRepository.save(entry.getFund()));
        fundSearchRepository.save(entry.getFund());

        entry.setCreation_date(new DateTime());
        Entry result = entryRepository.save(entry);
        entrySearchRepository.save(result);


        // Save fund history
        saveFundHistory(entry, oldFundAmount);

        return ResponseEntity.created(new URI("/api/entrys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("entry", entry.getCreation_date() == null ? "" :
                FunctionsUtil.convertDateToString(entry.getCreation_date().toDate(), Constants.GLOBAL_DATE_FORMAT)))
            .body(result);
    }

    /**
     * PUT  /entrys -> Updates an existing entry.
     */
    @RequestMapping(value = "/entrys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Entry> update(@Valid @RequestBody Entry entry) throws URISyntaxException {
        log.debug("REST request to update Entry : {}", entry);
        if (entry.getId() == null) {
            return create(entry);
        }

        if(entry.getFund()!=null && entry.getFund().getId()!=null){
            entry.setFund(fundRepository.findOne(entry.getFund().getId()));
        }

        // Add the charge amount to the old fund and decrease it from the current one.
        Entry chargeBeforeSave = entryRepository.findOne(entry.getId());
        Fund oldFund = chargeBeforeSave.getFund();
        log.info("oldFund value " + oldFund);
        if(oldFund!=null && oldFund.getAmount()!=null){
            log.info("oldFund before "+oldFund.getAmount().doubleValue());
            BigDecimal oldFundAmount = new BigDecimal(chargeBeforeSave.getFund().getAmount().doubleValue());
            oldFund.setAmount(new BigDecimal(oldFund.getAmount().doubleValue() + (chargeBeforeSave.getAmount().doubleValue() * chargeBeforeSave.getPrice().doubleValue())));
            log.info("oldFund after " + oldFund.getAmount().doubleValue());
            fundRepository.save(oldFund);
            fundSearchRepository.save(oldFund);
            if(oldFund.getId().equals(entry.getFund().getId())) {
                entry.setFund(oldFund);
            }

            // Save old fund history
            saveFundHistory(chargeBeforeSave, oldFundAmount);
        }
        log.info("entry.getFund() before "+entry.getFund().getAmount().doubleValue());
        log.info("entry.getAmount().doubleValue() before " + entry.getAmount().doubleValue());
        log.info("diff " + new BigDecimal(entry.getFund().getAmount().doubleValue() - entry.getAmount().doubleValue()));
        BigDecimal oldFundAmount = new BigDecimal(entry.getFund().getAmount().doubleValue());
        entry.getFund().setAmount(new BigDecimal(entry.getFund().getAmount().doubleValue() - (entry.getAmount().doubleValue() * entry.getPrice().doubleValue())));
        log.info("charge.getFund() after " + entry.getFund().getAmount().doubleValue());
        entry.setFund(fundRepository.save(entry.getFund()));
        fundSearchRepository.save(entry.getFund());

        Entry result = entryRepository.save(entry);
        entrySearchRepository.save(entry);

        // Save fund history
        saveFundHistory(entry, oldFundAmount);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert
                ("entry", entry.getCreation_date() == null ? "" : FunctionsUtil.convertDateToString(entry.getCreation_date().toDate(), Constants.GLOBAL_DATE_FORMAT)))
            .body(result);
    }

    /**
     * GET  /entrys -> get all the entrys.
     */
    @RequestMapping(value = "/entrys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Entry>> getAll(@RequestParam(value = "chargeId" , required = false) Long chargeId,
                                                 @RequestParam(value = "page" , required = false) Integer offset,
                                                 @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Entry> page = entryRepository.findAll(chargeId,PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/entrys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /entrys/:id -> get the "id" entry.
     */
    @RequestMapping(value = "/entrys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Entry> get(@PathVariable Long id) {
        log.debug("REST request to get Entry : {}", id);
        return Optional.ofNullable(entryRepository.findOne(id))
            .map(entry -> new ResponseEntity<>(
                entry,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /entrys/:id -> delete the "id" entry.
     */
    @RequestMapping(value = "/entrys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Entry : {}", id);
        Entry entry = entryRepository.findOne(id);
        log.debug("entry.getFund() "+entry.getFund());
        log.debug("entry.getFund().getAmount() "+entry.getFund().getAmount());
        log.debug("entry.getFund().getAmount().doubleValue() " + entry.getFund().getAmount().doubleValue());
        BigDecimal oldFundAmount = new BigDecimal(entry.getFund().getAmount().doubleValue());
        entry.getFund().setAmount(new BigDecimal(entry.getFund().getAmount().doubleValue() + (entry.getAmount().doubleValue() * entry.getPrice().doubleValue())));
        entry.setFund(fundRepository.save(entry.getFund()));
        fundSearchRepository.save(entry.getFund());

        // Save fund history
        saveFundHistory(entry, oldFundAmount);

        entryRepository.delete(id);
        entrySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("entry", entry.getCreation_date() == null ? "" : FunctionsUtil.convertDateToString(entry.getCreation_date().toDate(), Constants.GLOBAL_DATE_FORMAT))).build();
    }

    /**
     * SEARCH  /_search/entrys/:query -> search for the entry corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/entrys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Entry> search(@PathVariable String query) {
        return StreamSupport
            .stream(entrySearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public void saveFundHistory(Entry entry, BigDecimal oldFundAmount){
        Fund_history fund_history = new Fund_history();
        fund_history.setFund(entry.getFund());
        fund_history.setNew_amount(entry.getFund().getAmount());
        fund_history.setOld_amount(oldFundAmount);
        fund_history.setAmount_movement(new BigDecimal(fund_history.getNew_amount().doubleValue() - fund_history.getOld_amount().doubleValue()));
        fund_history.setType_operation(fund_history.getAmount_movement().doubleValue() >= 0d ? true : false);
        fund_history.setCharge(entry.getCharge());
        Optional<User>  user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        fund_history.setCreated_by(user.isPresent()?user.get():null);
        fund_history.setCreation_date(new DateTime());
        fund_historyRepository.save(fund_history);
    }

}
