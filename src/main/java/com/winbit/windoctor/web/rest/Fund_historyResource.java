package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Fund_history;
import com.winbit.windoctor.repository.Fund_historyRepository;
import com.winbit.windoctor.repository.search.Fund_historySearchRepository;
import com.winbit.windoctor.web.rest.util.HeaderUtil;
import com.winbit.windoctor.web.rest.util.PaginationUtil;
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
 * REST controller for managing Fund_history.
 */
@RestController
@RequestMapping("/api")
public class Fund_historyResource {

    private final Logger log = LoggerFactory.getLogger(Fund_historyResource.class);

    @Inject
    private Fund_historyRepository fund_historyRepository;

    @Inject
    private Fund_historySearchRepository fund_historySearchRepository;

    /**
     * POST  /fund_historys -> Create a new fund_history.
     */
    @RequestMapping(value = "/fund_historys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fund_history> create(@Valid @RequestBody Fund_history fund_history) throws URISyntaxException {
        log.debug("REST request to save Fund_history : {}", fund_history);
        if (fund_history.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new fund_history cannot already have an ID").body(null);
        }
        Fund_history result = fund_historyRepository.save(fund_history);
        fund_historySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fund_historys/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("fund_history", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /fund_historys -> Updates an existing fund_history.
     */
    @RequestMapping(value = "/fund_historys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fund_history> update(@Valid @RequestBody Fund_history fund_history) throws URISyntaxException {
        log.debug("REST request to update Fund_history : {}", fund_history);
        if (fund_history.getId() == null) {
            return create(fund_history);
        }
        Fund_history result = fund_historyRepository.save(fund_history);
        fund_historySearchRepository.save(fund_history);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("fund_history", fund_history.getId().toString()))
                .body(result);
    }

    /**
     * GET  /fund_historys -> get all the fund_historys.
     */
    @RequestMapping(value = "/fund_historys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fund_history>> getAll(@RequestParam(value = "fundId" , required = false) Long fundId,
                                    @RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Fund_history> page =  null;
        if(fundId!=null){
            log.info("fundId value "+fundId);
            page = fund_historyRepository.findAll(fundId,PaginationUtil.generatePageRequest(offset, limit));
        }else {
            page = fund_historyRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fund_historys", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fund_historys/:id -> get the "id" fund_history.
     */
    @RequestMapping(value = "/fund_historys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fund_history> get(@PathVariable Long id) {
        log.debug("REST request to get Fund_history : {}", id);
        return Optional.ofNullable(fund_historyRepository.findOne(id))
            .map(fund_history -> new ResponseEntity<>(
                fund_history,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fund_historys/:id -> delete the "id" fund_history.
     */
    @RequestMapping(value = "/fund_historys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Fund_history : {}", id);
        fund_historyRepository.delete(id);
        fund_historySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fund_history", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fund_historys/:query -> search for the fund_history corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/fund_historys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Fund_history> search(@PathVariable String query) {
        return StreamSupport
            .stream(fund_historySearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
