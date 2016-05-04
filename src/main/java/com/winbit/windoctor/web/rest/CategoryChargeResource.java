package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.CategoryCharge;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.repository.CategoryChargeRepository;
import com.winbit.windoctor.repository.search.CategoryChargeSearchRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import com.winbit.windoctor.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CategoryCharge.
 */
@RestController
@RequestMapping("/api")
public class CategoryChargeResource {

    private final Logger log = LoggerFactory.getLogger(CategoryChargeResource.class);

    @Inject
    private CategoryChargeRepository categoryChargeRepository;

    @Inject
    private CategoryChargeSearchRepository categoryChargeSearchRepository;

    /**
     * POST  /categoryCharges -> Create a new categoryCharge.
     */
    @RequestMapping(value = "/categoryCharges",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoryCharge> create(@Valid @RequestBody CategoryCharge categoryCharge) throws URISyntaxException {
        log.debug("REST request to save CategoryCharge : {}", categoryCharge);
        if (categoryCharge.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new categoryCharge cannot already have an ID").body(null);
        }
        categoryCharge.setStructure(new Structure());
        categoryCharge.getStructure().setId(SecurityUtils.getCurrerntStructure());
        CategoryCharge result = categoryChargeRepository.save(categoryCharge);
        categoryChargeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/categoryCharges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categoryCharge", result.getName()))
            .body(result);
    }

    /**
     * PUT  /categoryCharges -> Updates an existing categoryCharge.
     */
    @RequestMapping(value = "/categoryCharges",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoryCharge> update(@Valid @RequestBody CategoryCharge categoryCharge) throws URISyntaxException {
        log.debug("REST request to update CategoryCharge : {}", categoryCharge);
        if (categoryCharge.getId() == null) {
            return create(categoryCharge);
        }
        CategoryCharge result = categoryChargeRepository.save(categoryCharge);
        categoryChargeSearchRepository.save(categoryCharge);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categoryCharge", categoryCharge.getName().toString()))
            .body(result);
    }

    /**
     * GET  /categoryCharges -> get all the categoryCharges.
     */
    @RequestMapping(value = "/categoryCharges",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CategoryCharge>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                                 @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get CategoryCharges page per_page");
        Page<CategoryCharge> page = categoryChargeRepository.findAll(SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categoryCharges", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categoryCharges/:id -> get the "id" categoryCharge.
     */
    @RequestMapping(value = "/categoryCharges/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoryCharge> get(@PathVariable Long id) {
        log.debug("REST request to get CategoryCharge : {}", id);
        return Optional.ofNullable(categoryChargeRepository.findOne(id))
            .map(categoryCharge -> new ResponseEntity<>(
                categoryCharge,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categoryCharges/:id -> delete the "id" categoryCharge.
     */
    @RequestMapping(value = "/categoryCharges/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete CategoryCharge : {}", id);
        categoryChargeRepository.delete(id);
        categoryChargeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categoryCharge", id.toString())).build();
    }

    /**
     * SEARCH  /_search/categoryCharges/:query -> search for the categoryCharge corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/categoryCharges/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    /*public List<CategoryCharge> search(@PathVariable String query) {
        return StreamSupport
            .stream(categoryChargeSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }*/
    public ResponseEntity<List<CategoryCharge>> search(@PathVariable String query,@RequestParam(value = "page" , required = false) Integer offset,
                                                 @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<CategoryCharge> page;
        page = categoryChargeRepository.findAllMatchString(Constants.PERCENTAGE + query + Constants.PERCENTAGE, SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/categoryCharges", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
