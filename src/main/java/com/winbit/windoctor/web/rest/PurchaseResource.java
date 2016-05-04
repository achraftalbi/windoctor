package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.repository.FundRepository;
import com.winbit.windoctor.repository.Fund_historyRepository;
import com.winbit.windoctor.repository.PurchaseRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.FundSearchRepository;
import com.winbit.windoctor.repository.search.PurchaseSearchRepository;
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
 * REST controller for managing Purchase.
 */
@RestController
@RequestMapping("/api")
public class PurchaseResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseResource.class);

    @Inject
    private PurchaseRepository purchaseRepository;

    @Inject
    private PurchaseSearchRepository purchaseSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Fund_historyRepository fund_historyRepository;

    @Inject
    private FundRepository fundRepository;

    @Inject
    private FundSearchRepository fundSearchRepository;

    /**
     * POST  /purchases -> Create a new purchase.
     */
    @RequestMapping(value = "/purchases",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Purchase> create(@Valid @RequestBody Purchase purchase) throws URISyntaxException {
        log.debug("REST request to save Purchase : {}", purchase);
        if (purchase.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new purchase cannot already have an ID").body(null);
        }

        BigDecimal oldFundAmount = new BigDecimal(purchase.getPurchase_fund().getAmount().doubleValue());
        purchase.getPurchase_fund().setAmount(new BigDecimal(purchase.getPurchase_fund().getAmount().doubleValue() - (purchase.getAmount().doubleValue()*purchase.getPrice().doubleValue())));
        purchase.setPurchase_fund(fundRepository.save(purchase.getPurchase_fund()));
        fundSearchRepository.save(purchase.getPurchase_fund());

        purchase.setCreation_date(new DateTime());
        Purchase result = purchaseRepository.save(purchase);
        purchaseSearchRepository.save(result);


        // Save fund history
        saveFundHistory(purchase, oldFundAmount);

        return ResponseEntity.created(new URI("/api/purchases/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("purchase", purchase.getCreation_date() == null ? "" :
                    FunctionsUtil.convertDateToString(purchase.getCreation_date().toDate(), Constants.GLOBAL_DATE_FORMAT)))
                .body(result);
    }

    /**
     * PUT  /purchases -> Updates an existing purchase.
     */
    @RequestMapping(value = "/purchases",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Purchase> update(@Valid @RequestBody Purchase purchase) throws URISyntaxException {
        log.debug("REST request to update Purchase : {}", purchase);
        if (purchase.getId() == null) {
            return create(purchase);
        }

        // Add the product amount to the old fund and decrease it from the current one.
        Purchase productBeforeSave = purchaseRepository.findOne(purchase.getId());
        Fund oldFund = productBeforeSave.getPurchase_fund();
        log.info("oldFund value " + oldFund);
        if(oldFund!=null && oldFund.getAmount()!=null){
            log.info("oldFund before "+oldFund.getAmount().doubleValue());
            BigDecimal oldFundAmount = new BigDecimal(productBeforeSave.getPurchase_fund().getAmount().doubleValue());
            oldFund.setAmount(new BigDecimal(oldFund.getAmount().doubleValue() + (productBeforeSave.getAmount().doubleValue() * productBeforeSave.getPrice().doubleValue())));
            log.info("oldFund after " + oldFund.getAmount().doubleValue());
            fundRepository.save(oldFund);
            fundSearchRepository.save(oldFund);
            if(oldFund.getId().equals(purchase.getPurchase_fund().getId())) {
                purchase.setPurchase_fund(oldFund);
            }

            // Save old fund history
            saveFundHistory(productBeforeSave, oldFundAmount);
        }
        log.info("purchase.getFund() before "+purchase.getPurchase_fund().getAmount().doubleValue());
        log.info("purchase.getAmount().doubleValue() before " + purchase.getAmount().doubleValue());
        log.info("diff " + new BigDecimal(purchase.getPurchase_fund().getAmount().doubleValue() - purchase.getAmount().doubleValue()));
        BigDecimal oldFundAmount = new BigDecimal(purchase.getPurchase_fund().getAmount().doubleValue());
        purchase.getPurchase_fund().setAmount(new BigDecimal(purchase.getPurchase_fund().getAmount().doubleValue() - (purchase.getAmount().doubleValue() * purchase.getPrice().doubleValue())));
        log.info("product.getFund() after " + purchase.getPurchase_fund().getAmount().doubleValue());
        purchase.setPurchase_fund(fundRepository.save(purchase.getPurchase_fund()));
        fundSearchRepository.save(purchase.getPurchase_fund());

        Purchase result = purchaseRepository.save(purchase);
        purchaseSearchRepository.save(purchase);

        // Save fund history
        saveFundHistory(purchase, oldFundAmount);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert
                    ("purchase", purchase.getCreation_date() == null ? "" : FunctionsUtil.convertDateToString(purchase.getCreation_date().toDate(), Constants.GLOBAL_DATE_FORMAT)))
                .body(result);
    }

    /**
     * GET  /purchases -> get all the purchases.
     */
    @RequestMapping(value = "/purchases",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Purchase>> getAll(@RequestParam(value = "productId" , required = false) Long productId,
                                  @RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Purchase> page = purchaseRepository.findAll(productId,PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purchases", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /purchases/:id -> get the "id" purchase.
     */
    @RequestMapping(value = "/purchases/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Purchase> get(@PathVariable Long id) {
        log.debug("REST request to get Purchase : {}", id);
        return Optional.ofNullable(purchaseRepository.findOne(id))
            .map(purchase -> new ResponseEntity<>(
                purchase,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /purchases/:id -> delete the "id" purchase.
     */
    @RequestMapping(value = "/purchases/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Purchase : {}", id);
        Purchase purchase = purchaseRepository.findOne(id);
        log.debug("purchase.getPurchase_fund() "+purchase.getPurchase_fund());
        log.debug("purchase.getPurchase_fund().getAmount() "+purchase.getPurchase_fund().getAmount());
        log.debug("purchase.getPurchase_fund().getAmount().doubleValue() " + purchase.getPurchase_fund().getAmount().doubleValue());
        BigDecimal oldFundAmount = new BigDecimal(purchase.getPurchase_fund().getAmount().doubleValue());
        purchase.getPurchase_fund().setAmount(new BigDecimal(purchase.getPurchase_fund().getAmount().doubleValue() + (purchase.getAmount().doubleValue() * purchase.getPrice().doubleValue())));
        purchase.setPurchase_fund(fundRepository.save(purchase.getPurchase_fund()));
        fundSearchRepository.save(purchase.getPurchase_fund());

        // Save fund history
        saveFundHistory(purchase, oldFundAmount);

        purchaseRepository.delete(id);
        purchaseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("purchase", purchase.getCreation_date() == null ? "" : FunctionsUtil.convertDateToString(purchase.getCreation_date().toDate(), Constants.GLOBAL_DATE_FORMAT))).build();
    }

    /**
     * SEARCH  /_search/purchases/:query -> search for the purchase corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/purchases/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Purchase> search(@PathVariable String query) {
        return StreamSupport
            .stream(purchaseSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public void saveFundHistory(Purchase purchase, BigDecimal oldFundAmount){
        Fund_history fund_history = new Fund_history();
        fund_history.setFund(purchase.getPurchase_fund());
        fund_history.setNew_amount(purchase.getPurchase_fund().getAmount());
        fund_history.setOld_amount(oldFundAmount);
        fund_history.setAmount_movement(new BigDecimal(fund_history.getNew_amount().doubleValue() - fund_history.getOld_amount().doubleValue()));
        fund_history.setType_operation(fund_history.getAmount_movement().doubleValue() >= 0d ? true : false);
        fund_history.setProduct(purchase.getPurchase_product());
        Optional<User>  user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        fund_history.setCreated_by(user.isPresent()?user.get():null);
        fund_history.setCreation_date(new DateTime());
        fund_historyRepository.save(fund_history);
    }

}
