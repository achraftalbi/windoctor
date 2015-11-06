package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.domain.Doctor;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.DoctorRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.DoctorSearchRepository;
import com.winbit.windoctor.security.AuthoritiesConstants;
import com.winbit.windoctor.service.SessionService;
import com.winbit.windoctor.service.UserService;
import com.winbit.windoctor.web.rest.dto.DoctorDTO;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Doctor.
 */
@RestController
@RequestMapping("/api")
public class DoctorResource {

    private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

    @Inject
    private DoctorRepository doctorRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private SessionService sessionService;

    /**
     * POST  /doctors -> Create a new doctor.
     */
    @RequestMapping(value = "/doctors",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> create(@Valid @RequestBody DoctorDTO doctor,HttpSession session) throws URISyntaxException {
        log.debug("REST request to save Doctor : {}", doctor);

        return userRepository.findOneByLogin(doctor.getLogin())
            .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
            .orElseGet(() -> userRepository.findOneByEmail(doctor.getEmail())
                    .map(user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
                    .orElseGet(() -> {
                        User user = userService.createDoctorInformation(doctor.getLogin(), doctor.getPassword(),
                            doctor.getFirstName(), doctor.getLastName(), doctor.getEmail().toLowerCase(),
                            doctor.getLangKey(), doctor.getBlocked(), doctor.getActivated(), doctor.getPicture(), doctor.getStructure().getId());

                        return new ResponseEntity<>(HttpStatus.CREATED);
                    })
            );
    }

    /**
     * PUT  /doctors -> Updates an existing doctor.
     */
    @RequestMapping(value = "/doctors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Doctor> update(@Valid @RequestBody DoctorDTO doctor) throws URISyntaxException {
        log.debug("REST request to update Doctor : {}", doctor);
        User user = userService.updateDoctorInformation(doctor.getLogin(), doctor.getPassword(),
            doctor.getFirstName(), doctor.getLastName(), doctor.getEmail().toLowerCase(),
            doctor.getLangKey(), doctor.getBlocked(), doctor.getActivated(), doctor.getPicture(),doctor.getStructure().getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /doctors -> get all the doctors.
     */
    @RequestMapping(value = "/doctors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.ADMIN,AuthoritiesConstants.DOCTOR})
    public ResponseEntity<List<User>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<User> page;
        page = userService.findAllDoctors(PaginationUtil.generatePageRequest(offset, limit));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/doctors", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /doctors/:id -> get the "id" doctor.
     */
    @RequestMapping(value = "/doctors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<User> get(@PathVariable Long id) {
        log.debug("REST request to get Doctor : {}", id);
        return Optional.ofNullable(userRepository.findOne(id))
            .map(patient -> new ResponseEntity<>(
                patient,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /doctors/:id -> delete the "id" doctor.
     */
    @RequestMapping(value = "/doctors/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Doctor : {}", id);
        userRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("doctor", id.toString())).build();
    }

    /**
     * SEARCH  /_search/doctors/:query -> search for the doctor corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/doctors/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<Doctor> search(@PathVariable String query) {
        //TODO - mbf-06-11-2015 : fix search
        return null;
    }
}
