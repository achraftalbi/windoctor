package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.StructureRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.UserSearchRepository;
import com.winbit.windoctor.security.AuthoritiesConstants;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.service.UserService;
import com.winbit.windoctor.web.rest.dto.PatientDTO;
import com.winbit.windoctor.web.rest.util.ErrorCodes;
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
    private StructureRepository structureRepository;

    @Inject
    private UserService userService;

    @Inject
    private UserSearchRepository userSearchRepository;

    /**
     * POST  /patients -> Create a new patient.
     */
    @RequestMapping(value = "/patients",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> create(@Valid @RequestBody PatientDTO patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        if(patient.getEmail()==null || patient.getEmail().length()==0){
            return userRepository.findOneByLogin(patient.getLogin())
                .map(user -> new ResponseEntity<>(ErrorCodes.User.LOGIN_ALREADY_USED, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                            User user = userService.createPatientInformation(patient);
                            userSearchRepository.save(user);


                            return new ResponseEntity<>(HttpStatus.CREATED);
                        });
        }else{
            return userRepository.findOneByLogin(patient.getLogin())
                .map(user -> new ResponseEntity<>(ErrorCodes.User.LOGIN_ALREADY_USED, HttpStatus.BAD_REQUEST))
                .orElseGet(() -> userRepository.findOneByEmailAndStructure(patient.getEmail(), structureRepository.findOneById(SecurityUtils.getCurrerntStructure()))
                        .map(user -> new ResponseEntity<>(ErrorCodes.User.EMAIL_ALREADY_USED, HttpStatus.BAD_REQUEST))
                        .orElseGet(() -> {
                            User user = userService.createPatientInformation(patient);
                            userSearchRepository.save(user);


                            return new ResponseEntity<>(HttpStatus.CREATED);
                        })
                );
        }
    }

    /**
     * PUT  /patients -> Updates an existing patient.
     */
    @RequestMapping(value = "/patients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> update(@Valid @RequestBody PatientDTO patient) throws URISyntaxException {
        log.debug("REST request to update Patient : {}", patient);
        //TODO - mbf 27092015 - manage exception !
        User user = userService.updatePatientInformation(patient);
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
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<User> page;
        page = userService.findAllPatients(PaginationUtil.generatePageRequest(offset, limit));
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
        ResponseEntity<User> patientTmp = Optional.ofNullable(userRepository.findOne(id))
            .map(patient -> new ResponseEntity<>(
                patient,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        log.info("patient login "+patientTmp.getBody().getLogin());
        log.info("patient password "+patientTmp.getBody().getPassword());
        return patientTmp;
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
    /*public List<User> search(@PathVariable String query) {
        QueryStringQueryBuilder queryStringQueryBuilder = queryString(query);
        log.debug("patients  queryStringQueryBuilder " + queryStringQueryBuilder);






        return StreamSupport
            .stream(userRepository.findAllMatchString(Constants.PERCENTAGE + query + Constants.PERCENTAGE, AuthoritiesConstants.PATIENT, SecurityUtils.getCurrerntStructure()).spliterator(), false)
            .collect(Collectors.toList());
    }*/
    public ResponseEntity<List<User>> search(@PathVariable String query,@RequestParam(value = "page" , required = false) Integer offset,
                                             @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<User> page;
        page = userRepository.findAllMatchString(Constants.PERCENTAGE + query + Constants.PERCENTAGE, AuthoritiesConstants.PATIENT, SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/patients", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
