package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Attachment;
import com.winbit.windoctor.repository.AttachmentRepository;
import com.winbit.windoctor.repository.search.AttachmentSearchRepository;
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
 * REST controller for managing Attachment.
 */
@RestController
@RequestMapping("/api")
public class AttachmentResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    @Inject
    private AttachmentRepository attachmentRepository;

    @Inject
    private AttachmentSearchRepository attachmentSearchRepository;

    /**
     * POST  /attachments -> Create a new attachment.
     */
    @RequestMapping(value = "/attachments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attachment> create(@Valid @RequestBody Attachment attachment) throws URISyntaxException {
        log.debug("REST request to save Attachment : {}", attachment);
        if (attachment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new attachment cannot already have an ID").body(null);
        }
        Attachment result = attachmentRepository.save(attachment);
        attachmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/attachments/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("attachment", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /attachments -> Updates an existing attachment.
     */
    @RequestMapping(value = "/attachments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attachment> update(@Valid @RequestBody Attachment attachment) throws URISyntaxException {
        log.debug("REST request to update Attachment : {}", attachment);
        if (attachment.getId() == null) {
            return create(attachment);
        }
        Attachment result = attachmentRepository.save(attachment);
        attachmentSearchRepository.save(attachment);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("attachment", attachment.getId().toString()))
                .body(result);
    }

    /**
     * GET  /attachments -> get all the attachments.
     */
    @RequestMapping(value = "/attachments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Attachment>> getAll(@RequestParam(value = "treatmentId" , required = false) Long treatmentId, @RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Attachment> page = null;
        if (treatmentId!=null){
            log.debug("treatmentId part -> treatmentId:"+treatmentId);
            page = attachmentRepository.findByTreatment(treatmentId, PaginationUtil.generatePageRequest(offset, limit));
        }else{
            log.debug("last part -> treatmentId:"+treatmentId);
            page = attachmentRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attachments", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attachments/:id -> get the "id" attachment.
     */
    @RequestMapping(value = "/attachments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attachment> get(@PathVariable Long id) {
        log.debug("REST request to get Attachment : {}", id);
        return Optional.ofNullable(attachmentRepository.findOne(id))
            .map(attachment -> new ResponseEntity<>(
                attachment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /attachments/:id -> delete the "id" attachment.
     */
    @RequestMapping(value = "/attachments/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Attachment : {}", id);
        attachmentRepository.delete(id);
        attachmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("attachment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/attachments/:query -> search for the attachment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/attachments/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Attachment> search(@PathVariable String query) {
        return StreamSupport
            .stream(attachmentSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
