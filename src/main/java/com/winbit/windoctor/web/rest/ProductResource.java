package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.repository.Fund_historyRepository;
import com.winbit.windoctor.repository.ProductRepository;
import com.winbit.windoctor.repository.FundRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.FundSearchRepository;
import com.winbit.windoctor.repository.search.ProductSearchRepository;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.web.rest.util.HeaderUtil;
import org.joda.time.DateTime;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.winbit.windoctor.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    @Inject
    private ProductRepository productRepository;

    @Inject
    private FundRepository fundRepository;

    @Inject
    private Fund_historyRepository fund_historyRepository;

    @Inject
    private FundSearchRepository fundSearchRepository;

    @Inject
    private ProductSearchRepository productSearchRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /products -> Create a new product.
     */
    @RequestMapping(value = "/products",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) throws URISyntaxException {
        log.debug("REST request to save Product : {}", product);
        if (product.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new product cannot already have an ID").body(null);
        }
        product.setStructure(new Structure());
        product.getStructure().setId(SecurityUtils.getCurrerntStructure());
        // Decrease amount from the current fund.

        BigDecimal oldFundAmount = new BigDecimal(product.getFund().getAmount().doubleValue());
        product.getFund().setAmount(new BigDecimal(product.getFund().getAmount().doubleValue() - (product.getAmount().doubleValue()*product.getPrice().doubleValue())));
        product.setFund(fundRepository.save(product.getFund()));
        fundSearchRepository.save(product.getFund());

        Product result = productRepository.save(product);
        productSearchRepository.save(result);


        // Save fund history
        saveFundHistory(product, oldFundAmount);

        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("product", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /products -> Updates an existing product.
     */
    @RequestMapping(value = "/products",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Product> update(@Valid @RequestBody Product product) throws URISyntaxException {
        log.debug("REST request to update Product : {}", product);
        if (product.getId() == null) {
            return create(product);
        }
        // Add the product amount to the old fund and decrease it from the current one.
        Product productBeforeSave = productRepository.findOne(product.getId());
        Fund oldFund = productBeforeSave.getFund();
        log.info("oldFund value "+oldFund);
        if(oldFund!=null && oldFund.getAmount()!=null){
            log.info("oldFund before "+oldFund.getAmount().doubleValue());
            BigDecimal oldFundAmount = new BigDecimal(productBeforeSave.getFund().getAmount().doubleValue());
            oldFund.setAmount(new BigDecimal(oldFund.getAmount().doubleValue() + (productBeforeSave.getAmount().doubleValue() * productBeforeSave.getPrice().doubleValue())));
            log.info("oldFund after " + oldFund.getAmount().doubleValue());
            fundRepository.save(oldFund);
            fundSearchRepository.save(oldFund);
            if(oldFund.getId().equals(product.getFund().getId())) {
                product.setFund(oldFund);
            }

            // Save old fund history
            saveFundHistory(productBeforeSave, oldFundAmount);
        }
        log.info("product.getFund() before "+product.getFund().getAmount().doubleValue());
        log.info("product.getAmount().doubleValue() before " + product.getAmount().doubleValue());
        log.info("diff " + new BigDecimal(product.getFund().getAmount().doubleValue() - product.getAmount().doubleValue()));
        BigDecimal oldFundAmount = new BigDecimal(product.getFund().getAmount().doubleValue());
        product.getFund().setAmount(new BigDecimal(product.getFund().getAmount().doubleValue() - (product.getAmount().doubleValue() * product.getPrice().doubleValue())));
        log.info("product.getFund() after " + product.getFund().getAmount().doubleValue());
        product.setFund(fundRepository.save(product.getFund()));
        fundSearchRepository.save(product.getFund());

        Product result = productRepository.save(product);
        productSearchRepository.save(product);

        // Save fund history
        saveFundHistory(product, oldFundAmount);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("product", product.getId().toString()))
                .body(result);
    }

    /**
     * GET  /products -> get all the products.
     */
    @RequestMapping(value = "/products",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Product>> getAll(@RequestParam(value = "typeProductToGet", required = false) Long typeProductToGet,
                                                @RequestParam(value = "page" , required = false) Integer offset,
                                              @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get products page per_page");
        Page<Product> page;
        if(typeProductToGet==1l) {
            page = productRepository.findAll(SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        }else if(typeProductToGet==2l){
            page = productRepository.findAllThreshold(SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
        }else{
            page = productRepository.findAll(SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /products/:id -> get the "id" product.
     */
    @RequestMapping(value = "/products/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Product> get(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        return Optional.ofNullable(productRepository.findOne(id))
            .map(product -> new ResponseEntity<>(
                product,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /products/:id -> delete the "id" product.
     */
    @RequestMapping(value = "/products/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productRepository.delete(id);
        productSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("product", id.toString())).build();
    }

    /**
     * SEARCH  /_search/products/:query -> search for the product corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/products/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Product> search(@PathVariable String query) {
        return StreamSupport
            .stream(productSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


    public void saveFundHistory(Product product, BigDecimal oldFundAmount){
        Fund_history fund_history = new Fund_history();
        fund_history.setFund(product.getFund());
        fund_history.setNew_amount(product.getFund().getAmount());
        fund_history.setOld_amount(oldFundAmount);
        fund_history.setAmount_movement(new BigDecimal(fund_history.getNew_amount().doubleValue() - fund_history.getOld_amount().doubleValue()));
        fund_history.setType_operation(fund_history.getAmount_movement().doubleValue() >= 0d ? true : false);
        fund_history.setProduct(product);
        Optional<User>  user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        fund_history.setCreated_by(user.isPresent()?user.get():null);
        fund_history.setCreation_date(new DateTime());
        fund_historyRepository.save(fund_history);
    }

}
