package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.MailSetting;
import com.winbit.windoctor.repository.MailSettingRepository;
import com.winbit.windoctor.repository.search.MailSettingSearchRepository;
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
 * REST controller for managing MailSetting.
 */
@RestController
@RequestMapping("/api")
public class MailSettingResource {

    private final Logger log = LoggerFactory.getLogger(MailSettingResource.class);

    @Inject
    private MailSettingRepository mailSettingRepository;

    @Inject
    private MailSettingSearchRepository mailSettingSearchRepository;

    /**
     * POST  /mailSettings -> Create a new mailSetting.
     */
    @RequestMapping(value = "/mailSettings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MailSetting> createMailSetting(@RequestBody MailSetting mailSetting) throws URISyntaxException {
        log.debug("REST request to save MailSetting : {}", mailSetting);
        if (mailSetting.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new mailSetting cannot already have an ID").body(null);
        }
        MailSetting result = mailSettingRepository.save(mailSetting);
        mailSettingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mailSettings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mailSetting", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mailSettings -> Updates an existing mailSetting.
     */
    @RequestMapping(value = "/mailSettings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MailSetting> updateMailSetting(@RequestBody MailSetting mailSetting) throws URISyntaxException {
        log.debug("REST request to update MailSetting : {}", mailSetting);
        if (mailSetting.getId() == null) {
            return createMailSetting(mailSetting);
        }
        MailSetting result = mailSettingRepository.save(mailSetting);
        mailSettingSearchRepository.save(mailSetting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mailSetting", mailSetting.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mailSettings -> get all the mailSettings.
     */
    @RequestMapping(value = "/mailSettings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MailSetting>> getAllMailSettings(@RequestParam(value = "page" , required = false) Integer offset,
                                                                @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<MailSetting> page = mailSettingRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mailSettings",offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mailSettings/:id -> get the "id" mailSetting.
     */
    @RequestMapping(value = "/mailSettings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MailSetting> getMailSetting(@PathVariable Long id) {
        log.debug("REST request to get MailSetting : {}", id);
        return Optional.ofNullable(mailSettingRepository.findOne(id))
            .map(mailSetting -> new ResponseEntity<>(
                mailSetting,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mailSettings/:id -> delete the "id" mailSetting.
     */
    @RequestMapping(value = "/mailSettings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMailSetting(@PathVariable Long id) {
        log.debug("REST request to delete MailSetting : {}", id);
        mailSettingRepository.delete(id);
        mailSettingSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mailSetting", id.toString())).build();
    }

    /**
     * SEARCH  /_search/mailSettings/:query -> search for the mailSetting corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/mailSettings/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MailSetting> searchMailSettings(@PathVariable String query) {
        return StreamSupport
            .stream(mailSettingSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
