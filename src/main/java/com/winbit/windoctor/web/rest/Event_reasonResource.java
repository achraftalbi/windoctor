package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.Category;
import com.winbit.windoctor.domain.Event_reason;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.repository.Event_reasonRepository;
import com.winbit.windoctor.repository.search.Event_reasonSearchRepository;
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
 * REST controller for managing Event_reason.
 */
@RestController
@RequestMapping("/api")
public class Event_reasonResource {

    private final Logger log = LoggerFactory.getLogger(Event_reasonResource.class);

    @Inject
    private Event_reasonRepository event_reasonRepository;

    @Inject
    private Event_reasonSearchRepository event_reasonSearchRepository;

    /**
     * POST  /event_reasons -> Create a new event_reason.
     */
    @RequestMapping(value = "/event_reasons",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Event_reason> create(@Valid @RequestBody Event_reason event_reason) throws URISyntaxException {
        log.debug("REST request to save Event_reason : {}", event_reason);
        if (event_reason.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new event_reason cannot already have an ID").body(null);
        }
        event_reason.setStructure(new Structure());
        event_reason.getStructure().setId(SecurityUtils.getCurrerntStructure());
        Event_reason result = event_reasonRepository.save(event_reason);
        event_reasonSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/event_reasons/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("event_reason", result.getDescription().toString()))
                .body(result);
    }

    /**
     * PUT  /event_reasons -> Updates an existing event_reason.
     */
    @RequestMapping(value = "/event_reasons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Event_reason> update(@Valid @RequestBody Event_reason event_reason) throws URISyntaxException {
        log.debug("REST request to update Event_reason : {}", event_reason);
        if (event_reason.getId() == null) {
            return create(event_reason);
        }
        event_reason.setStructure(new Structure());
        event_reason.getStructure().setId(SecurityUtils.getCurrerntStructure());
        Event_reason result = event_reasonRepository.save(event_reason);
        event_reasonSearchRepository.save(event_reason);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("event_reason", event_reason.getDescription().toString()))
                .body(result);
    }

    /**
     * GET  /event_reasons -> get all the event_reasons.
     */
    @RequestMapping(value = "/event_reasons",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    /*public List<Event_reason> getAll() {
        log.debug("REST request to get all Event_reasons");
        return event_reasonRepository.findAll();
    }*/
    public ResponseEntity<List<Event_reason>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                                 @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get Event_reasons page per_page");
        Page<Event_reason> page = event_reasonRepository.findAll(SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/event_reasons", offset, limit);
        log.info("Event_reason size list " + page.getSize());
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /event_reasons/:id -> get the "id" event_reason.
     */
    @RequestMapping(value = "/event_reasons/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Event_reason> get(@PathVariable Long id) {
        log.debug("REST request to get Event_reason : {}", id);
        return Optional.ofNullable(event_reasonRepository.findOne(id))
            .map(event_reason -> new ResponseEntity<>(
                event_reason,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /event_reasons/:id -> delete the "id" event_reason.
     */
    @RequestMapping(value = "/event_reasons/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Event_reason : {}", id);
        event_reasonRepository.delete(id);
        event_reasonSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("event_reason", id.toString())).build();
    }

    /**
     * SEARCH  /_search/event_reasons/:query -> search for the event_reason corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/event_reasons/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Event_reason>> search(@PathVariable String query,@RequestParam(value = "page" , required = false) Integer offset,
                                                 @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Event_reason> page;
        page = event_reasonRepository.findAllMatchString(Constants.PERCENTAGE + query + Constants.PERCENTAGE, SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/event_reasons", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
