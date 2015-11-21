package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.MailType;
import com.winbit.windoctor.repository.MailTypeRepository;
import com.winbit.windoctor.repository.search.MailTypeSearchRepository;
import com.winbit.windoctor.security.AuthoritiesConstants;
import com.winbit.windoctor.web.rest.util.HeaderUtil;
import com.winbit.windoctor.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
 * REST controller for managing MailType.
 */
@RestController
@RequestMapping("/api")
public class MailTypeResource {

    private final Logger log = LoggerFactory.getLogger(MailTypeResource.class);

    @Inject
    private MailTypeRepository mailTypeRepository;

    @Inject
    private MailTypeSearchRepository mailTypeSearchRepository;

    /**
     * POST  /mailTypes -> Create a new mailType.
     */
    @RequestMapping(value = "/mailTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<MailType> createMailType(@Valid @RequestBody MailType mailType) throws URISyntaxException {
        log.debug("REST request to save MailType : {}", mailType);
        if (mailType.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new mailType cannot already have an ID").body(null);
        }
        MailType result = mailTypeRepository.save(mailType);
        mailTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mailTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mailType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mailTypes -> Updates an existing mailType.
     */
    @RequestMapping(value = "/mailTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<MailType> updateMailType(@Valid @RequestBody MailType mailType) throws URISyntaxException {
        log.debug("REST request to update MailType : {}", mailType);
        if (mailType.getId() == null) {
            return createMailType(mailType);
        }
        MailType result = mailTypeRepository.save(mailType);
        mailTypeSearchRepository.save(mailType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mailType", mailType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mailTypes -> get all the mailTypes.
     */
    @RequestMapping(value = "/mailTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MailType>> getAllMailTypes(@RequestParam(value = "page" , required = false) Integer offset,
                                                          @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<MailType> page = mailTypeRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mailTypes", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mailTypes/:id -> get the "id" mailType.
     */
    @RequestMapping(value = "/mailTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<MailType> getMailType(@PathVariable Long id) {
        log.debug("REST request to get MailType : {}", id);
        return Optional.ofNullable(mailTypeRepository.findOne(id))
            .map(mailType -> new ResponseEntity<>(
                mailType,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mailTypes/:id -> delete the "id" mailType.
     */
    @RequestMapping(value = "/mailTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteMailType(@PathVariable Long id) {
        log.debug("REST request to delete MailType : {}", id);
        mailTypeRepository.delete(id);
        mailTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mailType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/mailTypes/:query -> search for the mailType corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/mailTypes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MailType> searchMailTypes(@PathVariable String query) {
        return StreamSupport
            .stream(mailTypeSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());


    }
}
