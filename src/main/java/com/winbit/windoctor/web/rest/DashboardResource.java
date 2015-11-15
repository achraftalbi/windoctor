package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Dashboard;
import com.winbit.windoctor.repository.DashboardRepository;
import com.winbit.windoctor.repository.search.DashboardSearchRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Dashboard.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    @Inject
    private DashboardRepository dashboardRepository;

    @Inject
    private DashboardSearchRepository dashboardSearchRepository;

    /**
     * POST  /dashboards -> Create a new dashboard.
     */
    @RequestMapping(value = "/dashboards",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dashboard> create(@RequestBody Dashboard dashboard) throws URISyntaxException {
        log.debug("REST request to save Dashboard : {}", dashboard);
        if (dashboard.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new dashboard cannot already have an ID").body(null);
        }
        Dashboard result = dashboardRepository.save(dashboard);
        dashboardSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dashboards/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("dashboard", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /dashboards -> Updates an existing dashboard.
     */
    @RequestMapping(value = "/dashboards",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dashboard> update(@RequestBody Dashboard dashboard) throws URISyntaxException {
        log.debug("REST request to update Dashboard : {}", dashboard);
        if (dashboard.getId() == null) {
            return create(dashboard);
        }
        Dashboard result = dashboardRepository.save(dashboard);
        dashboardSearchRepository.save(dashboard);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("dashboard", dashboard.getId().toString()))
                .body(result);
    }

    /**
     * GET  /dashboards -> get all the dashboards.
     */
    @RequestMapping(value = "/dashboards",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Dashboard>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Dashboard> page = dashboardRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dashboards", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dashboards/:id -> get the "id" dashboard.
     */
    @RequestMapping(value = "/dashboards/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dashboard> get(@PathVariable Long id) {
        log.debug("REST request to get Dashboard : {}", id);
        return Optional.ofNullable(dashboardRepository.findOne(id))
            .map(dashboard -> new ResponseEntity<>(
                dashboard,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dashboards/:id -> delete the "id" dashboard.
     */
    @RequestMapping(value = "/dashboards/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Dashboard : {}", id);
        dashboardRepository.delete(id);
        dashboardSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dashboard", id.toString())).build();
    }

    /**
     * SEARCH  /_search/dashboards/:query -> search for the dashboard corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/dashboards/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Dashboard> search(@PathVariable String query) {
        return StreamSupport
            .stream(dashboardSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
