package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Dashboard;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.repository.DashboardRepository;
import com.winbit.windoctor.repository.StructureRepository;
import com.winbit.windoctor.repository.search.DashboardSearchRepository;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.web.rest.dto.DashboardDTO;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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

    @Inject
    private StructureRepository structureRepository;

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
    /*@RequestMapping(value = "/dashboards",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Dashboard>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Dashboard> page = dashboardRepository.findAllByYear(2015l);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dashboards", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/dashboards",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DashboardDTO> getAll(@RequestParam(value = "typeDashboard", required = false) Long typeDashboard,
                                  @RequestParam(value = "year", required = false) Long year)
        throws URISyntaxException {
        log.debug("REST request to get all Dashboards");
        List<Dashboard> dashboardList = null;
        DashboardDTO dashboardDTO= new DashboardDTO();
        List<Integer> years= new ArrayList<Integer>();
        Structure structure = structureRepository.findOneById(SecurityUtils.getCurrerntStructure());
        DateTime structureDateTime = structure.getCreation_date();
        DateTime currentDateTime = new DateTime();
        if(structureDateTime == null || structureDateTime.isAfter(currentDateTime)){
            years.add(currentDateTime.getYear());
        }else{
            int structureYear = structureDateTime.getYear();
            int currentYear = currentDateTime.getYear();
            for (int i = currentYear; i >= structureYear ; i--) {
                years.add(i);
            }
        }
        if(year==null){
            year = new Long(currentDateTime.getYear());
        }
        if(typeDashboard==null){
            dashboardList = dashboardRepository.findBudgetByYear(year, SecurityUtils.getCurrerntStructure());
        }else if(typeDashboard==1l) {
            dashboardList = dashboardRepository.findBudgetByYear(year, SecurityUtils.getCurrerntStructure());
        }else if(typeDashboard==2l){
            dashboardList = dashboardRepository.findPatientsByYearMonths(year, SecurityUtils.getCurrerntStructure());
        }else{
            dashboardList = dashboardRepository.findBudgetByYear(year, SecurityUtils.getCurrerntStructure());
        }




        dashboardDTO.setDashboardList(dashboardList);
        dashboardDTO.setYears(years);
        return new ResponseEntity<>(
            dashboardDTO,
            HttpStatus.OK);
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
