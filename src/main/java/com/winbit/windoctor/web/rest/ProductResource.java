package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.repository.*;
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
    private ProductSearchRepository productSearchRepository;

    @Inject
    private PurchaseRepository purchaseRepository;

    @Inject
    private ConsumptionRepository consumptionRepository;


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

        Product result = productRepository.save(product);
        productSearchRepository.save(result);


        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("product", result.getName().toString()))
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

        Product result = productRepository.save(product);
        productSearchRepository.save(product);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("product", product.getName().toString()))
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
                                                @RequestParam(value = "page", required = false) Integer offset,
                                                @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get products page per_page");
        Page<Product> page;
        if (typeProductToGet == 2l) {
            page = productRepository.findAllThreshold(SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
        } else {
            page = productRepository.findAll(SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
        }
        List<Product> products = page.getContent();
        for (Product product : products) {
            Purchase purchase = purchaseRepository.findTotalProduct(product.getId());
            Consumption consumption = consumptionRepository.findTotalConsumption(product.getId());
            product.setPrice(purchase.getPrice());
            product.setAmount(new BigDecimal(purchase.getAmount().doubleValue() - consumption.getAmount().doubleValue()));
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
        Product productTmp =productRepository.findOne(id);
        Purchase purchase = purchaseRepository.findTotalProduct(productTmp.getId());
        productTmp.setPrice(purchase.getPrice());
        productTmp.setAmount(purchase.getAmount());

        return Optional.ofNullable(productTmp)
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
    /*public List<Product> search(@PathVariable String query) {
        return StreamSupport
            .stream(productSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }*/
    public ResponseEntity<List<Product>> search(@PathVariable String query, @RequestParam(value = "page", required = false) Integer offset,
                                                @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Product> page;
        page = productRepository.findAllMatchString(Constants.PERCENTAGE + query + Constants.PERCENTAGE, SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
        List<Product> products = page.getContent();
        for (Product product : products) {
            Purchase purchase = purchaseRepository.findTotalProduct(product.getId());
            product.setPrice(purchase.getPrice());
            product.setAmount(purchase.getAmount());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/products", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
