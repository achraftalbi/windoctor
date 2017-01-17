package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Plan;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.PlanRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.PlanSearchRepository;
import com.winbit.windoctor.security.SecurityUtils;
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
 * REST controller for managing Plan.
 */
@RestController
@RequestMapping("/api")
public class PlanResource {

    private final Logger log = LoggerFactory.getLogger(PlanResource.class);

    @Inject
    private PlanRepository planRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PlanSearchRepository planSearchRepository;

    /**
     * POST  /plans -> Create a new plan.
     */
    @RequestMapping(value = "/plans",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Plan> create(@Valid @RequestBody Plan plan) throws URISyntaxException {
        log.debug("REST request to save Plan : {}", plan);
        if (plan.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new plan cannot already have an ID").body(null);
        }
        Plan result = planRepository.save(plan);
        planSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/plans/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("plan", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /plans -> Updates an existing plan.
     */
    @RequestMapping(value = "/plans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Plan> update(@Valid @RequestBody Plan plan) throws URISyntaxException {
        log.debug("REST request to update Plan : {}", plan);
        if (plan.getId() == null) {
            return create(plan);
        }
        Plan result = planRepository.save(plan);
        planSearchRepository.save(plan);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("plan", plan.getId().toString()))
                .body(result);
    }

    /**
     * GET  /plans -> get all the plans.
     */
    @RequestMapping(value = "/plans",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Plan>> getAll(@RequestParam(value = "patientId", required = false) Long patientId,
                                             @RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        List<Plan> plansArchived = planRepository.findAll(patientId, SecurityUtils.getCurrerntStructure());
        return new ResponseEntity<>(plansArchived, HttpStatus.OK);
    }

    /**
     * GET  /plans/:id -> get the "id" plan.
     */
    @RequestMapping(value = "/plans/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Plan> get(@PathVariable Long id) {
        log.debug("REST request to get Plan : {}", id);
        Plan planTmp = planRepository.findOne(id);
        if(planTmp!=null && SecurityUtils.getCurrerntStructure().equals(planTmp.getStructure().getId())){
            planTmp.setArchive(true);
            User patient = userRepository.findOne(planTmp.getUser_id());
            patient.setPlan(null);
            userRepository.save(patient);
            planRepository.save(planTmp);
        }
        return Optional.ofNullable(planTmp)
            .map(plan -> new ResponseEntity<>(
                plan,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /plans/:id -> delete the "id" plan.
     */
    @RequestMapping(value = "/plans/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Plan : {}", id);
        planRepository.delete(id);
        planSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("plan", id.toString())).build();
    }

    /**
     * SEARCH  /_search/plans/:query -> search for the plan corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/plans/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Plan> search(@PathVariable String query) {
        return StreamSupport
            .stream(planSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
