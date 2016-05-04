package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.CategoryAct;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.repository.CategoryActRepository;
import com.winbit.windoctor.repository.search.CategoryActSearchRepository;
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
 * REST controller for managing CategoryAct.
 */
@RestController
@RequestMapping("/api")
public class CategoryActResource {

    private final Logger log = LoggerFactory.getLogger(CategoryActResource.class);

    @Inject
    private CategoryActRepository categoryActRepository;

    @Inject
    private CategoryActSearchRepository categoryActSearchRepository;

    /**
     * POST  /categoryActs -> Create a new categoryAct.
     */
    @RequestMapping(value = "/categoryActs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoryAct> create(@Valid @RequestBody CategoryAct categoryAct) throws URISyntaxException {
        log.debug("REST request to save CategoryAct : {}", categoryAct);
        if (categoryAct.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new categoryAct cannot already have an ID").body(null);
        }
        categoryAct.setStructure(new Structure());
        categoryAct.getStructure().setId(SecurityUtils.getCurrerntStructure());
        CategoryAct result = categoryActRepository.save(categoryAct);
        categoryActSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/categoryActs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categoryAct", result.getName()))
            .body(result);
    }

    /**
     * PUT  /categoryActs -> Updates an existing categoryAct.
     */
    @RequestMapping(value = "/categoryActs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoryAct> update(@Valid @RequestBody CategoryAct categoryAct) throws URISyntaxException {
        log.debug("REST request to update CategoryAct : {}", categoryAct);
        if (categoryAct.getId() == null) {
            return create(categoryAct);
        }
        CategoryAct result = categoryActRepository.save(categoryAct);
        categoryActSearchRepository.save(categoryAct);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categoryAct", categoryAct.getName().toString()))
            .body(result);
    }

    /**
     * GET  /categoryActs -> get all the categoryActs.
     */
    @RequestMapping(value = "/categoryActs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CategoryAct>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                                       @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get CategoryActs page per_page");
        Page<CategoryAct> page = categoryActRepository.findAll(SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categoryActs", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categoryActs/:id -> get the "id" categoryAct.
     */
    @RequestMapping(value = "/categoryActs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoryAct> get(@PathVariable Long id) {
        log.debug("REST request to get CategoryAct : {}", id);
        return Optional.ofNullable(categoryActRepository.findOne(id))
            .map(categoryAct -> new ResponseEntity<>(
                categoryAct,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categoryActs/:id -> delete the "id" categoryAct.
     */
    @RequestMapping(value = "/categoryActs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete CategoryAct : {}", id);
        categoryActRepository.delete(id);
        categoryActSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categoryAct", id.toString())).build();
    }

    /**
     * SEARCH  /_search/categoryActs/:query -> search for the categoryAct corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/categoryActs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    /*public List<CategoryAct> search(@PathVariable String query) {
        return StreamSupport
            .stream(categoryActSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }*/
    public ResponseEntity<List<CategoryAct>> search(@PathVariable String query,@RequestParam(value = "page" , required = false) Integer offset,
                                                       @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<CategoryAct> page;
        page = categoryActRepository.findAllMatchString(Constants.PERCENTAGE + query + Constants.PERCENTAGE, SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/categoryActs", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
