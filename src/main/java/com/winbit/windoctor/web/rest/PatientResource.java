package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.UserSearchRepository;
import com.winbit.windoctor.security.AuthoritiesConstants;
import com.winbit.windoctor.service.SessionService;
import com.winbit.windoctor.service.UserService;
import com.winbit.windoctor.web.rest.dto.UserDTO;
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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Patient.
 */
@RestController
@RequestMapping("/api")
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private SessionService sessionService;

    /**
     * POST  /patients -> Create a new patient.
     */
    @RequestMapping(value = "/patients",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> create(@Valid @RequestBody UserDTO patient,HttpSession session) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        return userRepository.findOneByLogin(patient.getLogin())
            .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
            .orElseGet(() -> userRepository.findOneByEmail(patient.getEmail())
                    .map(user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
                    .orElseGet(() -> {
                        User user = userService.createPatientInformation(patient.getLogin(), patient.getPassword(),
                            patient.getFirstName(), patient.getLastName(), patient.getEmail().toLowerCase(),
                            patient.getLangKey(),patient.getBlocked(), patient.getActivated(), patient.getPicture(),sessionService.getCurrentStructure(session));
                        userSearchRepository.save(user);

                        return new ResponseEntity<>(HttpStatus.CREATED);
                    })
            );
    }

    /**
     * PUT  /patients -> Updates an existing patient.
     */
    @RequestMapping(value = "/patients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> update(@Valid @RequestBody UserDTO patient) throws URISyntaxException {
        log.debug("REST request to update Patient : {}", patient);
        //TODO - mbf 27092015 - manage exception !
        User user = userService.updatePatientInformation(patient.getLogin(), patient.getPassword(),
            patient.getFirstName(), patient.getLastName(), patient.getEmail().toLowerCase(),
            patient.getLangKey(), patient.getBlocked(), patient.getActivated(), patient.getPicture());
        userSearchRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /patients -> get all the patients.
     */
    @RequestMapping(value = "/patients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<User>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit, HttpSession session)
        throws URISyntaxException {
        Page<User> page;
        Structure currentStructure = sessionService.getCurrentStructure(session);
        if(currentStructure == null){
            page = userRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        } else {
            page = userRepository.findAll(AuthoritiesConstants.PATIENT, currentStructure.getId(), PaginationUtil.generatePageRequest(offset, limit));
        }

        //TODO - mbf-27092015 : be aware for perfomance here !!
        // Boufnichel i don't un derstand this part of code, i commented it, i need more explications.
        //userSearchRepository.save(page.getContent());
        for (User user:((List<User>)page.getContent())){
            user.setNoEvents(user.getEvents()==null || user.getEvents().size()==0);
            log.debug("user.getNoEvents"+user.getNoEvents()+" user getEvents "+user.getEvents());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/patients", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /patients/:id -> get the "id" patient.
     */
    @RequestMapping(value = "/patients/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> get(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        return Optional.ofNullable(userRepository.findOne(id))
            .map(patient -> new ResponseEntity<>(
                patient,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /patients/:id -> delete the "id" patient.
     */
    @RequestMapping(value = "/patients/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Patient : {}", id);
        userRepository.delete(id);
        userSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("patient", id.toString())).build();
    }

    /**
     * SEARCH  /_search/patients/:query -> search for the patient corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/patients/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> search(@PathVariable String query) {
        return StreamSupport
            .stream(userSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
