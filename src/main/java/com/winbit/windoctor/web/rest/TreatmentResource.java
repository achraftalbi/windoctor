package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.common.WinDoctorConstants;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.*;
import com.winbit.windoctor.repository.*;
import com.winbit.windoctor.repository.search.FundSearchRepository;
import com.winbit.windoctor.repository.search.TreatmentSearchRepository;
import com.winbit.windoctor.security.AuthoritiesConstants;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.web.rest.dto.TreatmentDTO;
import com.winbit.windoctor.web.rest.util.FunctionsUtil;
import com.winbit.windoctor.web.rest.util.HeaderUtil;
import com.winbit.windoctor.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.winbit.windoctor.web.rest.util.FunctionsUtil.addDays;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Treatment.
 */
@RestController
@RequestMapping("/api")
public class TreatmentResource {

    private final Logger log = LoggerFactory.getLogger(TreatmentResource.class);

    @Inject
    private TreatmentRepository treatmentRepository;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private FundRepository fundRepository;

    @Inject
    private Fund_historyRepository fund_historyRepository;

    @Inject
    private FundSearchRepository fundSearchRepository;

    @Inject
    private TreatmentSearchRepository treatmentSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PlanRepository planRepository;

    @Inject
    private PlanResource planResource;

    /**
     * POST  /treatments -> Create a new treatment.
     */
    @RequestMapping(value = "/treatments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Treatment> create(@Valid @RequestBody Treatment treatment) throws URISyntaxException {
        log.debug("REST request to save Treatment : {}", treatment);
        if (treatment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new treatment cannot already have an ID").body(null);
        }

        if(treatment.getFund()!=null && treatment.getFund().getId()!=null){
            treatment.setFund(fundRepository.findOne(treatment.getFund().getId()));
        }

        // Treatment done only for the plan.
        if(treatment.getStatus()!=null && Constants.STATUS_IN_PROGRESS.equals(treatment.getStatus().getId())){
                Event event = eventRepository.findOne(treatment.getEvent().getId());
                treatment.setPaid_price(new BigDecimal(0l));
                if(event.getUser().getPlan()!=null && event.getUser().getPlan().getId()!=null){
                    treatment.setPlan(event.getUser().getPlan());
                }else{
                    Plan plan = new Plan();
                    plan.setUser_id(event.getUser().getId());
                    plan.setStructure(new Structure());
                    plan.getStructure().setId(SecurityUtils.getCurrerntStructure());
                    plan.setCreation_date(new DateTime());
                    BigDecimal planNumber = planRepository.findPlanNumber(event.getUser().getId(), SecurityUtils.getCurrerntStructure());
                    plan.setNumber(new BigDecimal(planNumber == null ? 0 + 1 : planNumber.intValue() + 1));
                    ResponseEntity<Plan> responseEntity = planResource.create(plan);
                    event.getUser().setPlan(responseEntity.getBody());
                    userRepository.save(event.getUser());
                    treatment.setPlan(responseEntity.getBody());
                }
        }

        // Treatment if price or paid price is null.
        if(treatment.getPrice()==null){
            treatment.setPrice(new BigDecimal(0l));
        }
        if(treatment.getPaid_price()==null){
            treatment.setPaid_price(new BigDecimal(0l));
        }

        // Mange funds with treatments Begin
        BigDecimal oldFundAmount = new BigDecimal(treatment.getFund().getAmount().doubleValue());
        treatment.getFund().setAmount(new BigDecimal(treatment.getFund().getAmount().doubleValue() + treatment.getPaid_price().doubleValue()));
        treatment.setFund(fundRepository.save(treatment.getFund()));
        fundSearchRepository.save(treatment.getFund());
        // Mange funds with treatments End

        Treatment result = treatmentRepository.save(treatment);
        treatmentSearchRepository.save(result);
        // Save fund history
        saveFundHistory(treatment, oldFundAmount);


        return ResponseEntity.created(new URI("/api/treatments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("treatment",
                treatment.getEvent().getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(treatment.getEvent().getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
            .body(result);
    }

    /**
     * PUT  /treatments -> Updates an existing treatment.
     */
    @RequestMapping(value = "/treatments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Treatment> update(@Valid @RequestBody Treatment treatment) throws URISyntaxException {
        log.debug("REST request to update Treatment : {}", treatment);
        if (treatment.getId() == null) {
            return create(treatment);
        }

        if(treatment.getFund()!=null && treatment.getFund().getId()!=null){
            treatment.setFund(fundRepository.findOne(treatment.getFund().getId()));
        }
        log.info("treatment sorting_key " + treatment.getSorting_key());
        log.info("treatment getId " + treatment.getId());
        // Mange funds with treatments Begin
        Treatment treatmentBeforeSave = treatmentRepository.findOne(treatment.getId());
        Fund oldFund = treatmentBeforeSave.getFund();
        log.info("oldFund value " + oldFund);
        if (oldFund != null && oldFund.getAmount() != null) {
            BigDecimal oldFundAmount = new BigDecimal(treatmentBeforeSave.getFund().getAmount().doubleValue());
            log.info("oldFund before " + oldFund.getAmount().doubleValue());
            oldFund.setAmount(new BigDecimal(oldFund.getAmount().doubleValue() - treatmentBeforeSave.getPaid_price().doubleValue()));
            log.info("oldFund after " + oldFund.getAmount().doubleValue());

            //Manage the case the some funds
            if (oldFund.getId().equals(treatment.getFund().getId())) {
                oldFund.setAmount(new BigDecimal(oldFund.getAmount().doubleValue() + treatment.getPaid_price().doubleValue()));
            }

            fundRepository.save(oldFund);
            fundSearchRepository.save(oldFund);

            // Save old fund history
            saveFundHistory(treatmentBeforeSave, oldFundAmount);
        }
        log.info("treatment.getFund() before " + treatment.getFund().getAmount().doubleValue());
        log.info("treatment.getPaid_price().doubleValue() before " + treatment.getPaid_price().doubleValue());
        log.info("Add " + new BigDecimal(treatment.getFund().getAmount().doubleValue() + treatment.getPaid_price().doubleValue()));
        if (oldFund == null || (!oldFund.getId().equals(treatment.getFund().getId()))) {
            BigDecimal oldFundAmount = new BigDecimal(treatment.getFund().getAmount().doubleValue());
            treatment.getFund().setAmount(new BigDecimal(treatment.getFund().getAmount().doubleValue() + treatment.getPaid_price().doubleValue()));
            treatment.setFund(fundRepository.save(treatment.getFund()));
            fundSearchRepository.save(treatment.getFund());

            // Save fund history
            saveFundHistory(treatment, oldFundAmount);
        }
        log.info("treatment.getFund() after " + treatment.getFund().getAmount().doubleValue());
        // Mange funds with treatments End

        log.info("segond treatment sorting_key " + treatment.getSorting_key());
        Treatment result = treatmentRepository.save(treatment);
        treatmentSearchRepository.save(treatment);
        if(treatment.getStatus()!=null &&
            (Constants.STATUS_IN_PROGRESS.equals(treatment.getStatus().getId())
                || Constants.STATUS_EXECUTED.equals(treatment.getStatus().getId()))
            && treatment.getSorting_key()!=null
            && (!treatment.getSorting_key().equals(treatmentBeforeSave.getSorting_key()))
            ){
            treatmentRepository.afterUpdateTreatmentSorting(treatment.getId());
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("treatment",
                treatment.getEvent().getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(treatment.getEvent().getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
            .body(result);
    }

    /**
     * GET  /treatments -> get all the treatments.
     */
    @RequestMapping(value = "/treatments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TreatmentDTO> getAll(@RequestParam(value = "eventId", required = false) Long eventId, @RequestParam(value = "patientId", required = false) Long patientId, @RequestParam(value = "page", required = false) Integer offset,
                                               @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Treatment> page = null;
        Page<Treatment> pagePlan = null;
        if (patientId != null) {
            log.debug("patientId part -> eventId:" + eventId + " patientId:" + patientId);
            Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
            if (offset == null || limit == null) {
                page = treatmentRepository.findByPatient(patientId, null);
            } else {
                page = treatmentRepository.findByPatient(patientId, PaginationUtil.generatePageRequest(offset, limit));
            }
            Treatment totalPatientTreatments = treatmentRepository.findTotalPatientTreatments(patientId);
            totalPatientTreatments.setId(-1l);
            log.info("Total paid price :" + totalPatientTreatments.getPrice());
            List<Treatment> newTreatmentList = new ArrayList<Treatment>(page.getContent());
            newTreatmentList.add(totalPatientTreatments);
            page = new PageImpl<Treatment>(newTreatmentList, pageable, page.getTotalElements());

            /**
             * Treatment done for the plan.
             */
            if (offset == null || limit == null) {
                pagePlan = treatmentRepository.findByPatientPlan(patientId, null);
            } else {
                pagePlan = treatmentRepository.findByPatientPlan(patientId, PaginationUtil.generatePageRequest(offset, limit));
            }
            Treatment totalPatientTreatmentsPlan = treatmentRepository.findTotalPatientTreatmentsPlan(patientId);
            totalPatientTreatmentsPlan.setId(-1l);
            totalPatientTreatmentsPlan.setSorting_key(-1l);
            log.info("Plan Total paid price :" + totalPatientTreatmentsPlan.getPrice());
            List<Treatment> newTreatmentPlanList = new ArrayList<Treatment>(pagePlan.getContent());
            for(Treatment treatment:newTreatmentPlanList){
                /*if(treatment.getStatus()!=null
                    && Constants.STATUS_EXECUTED.equals(treatment.getStatus().getId())){
                    treatment.setElements(null);
                }*/
                log.info("Plan treatment.getSorting_key() " + treatment.getSorting_key());
            }
            newTreatmentPlanList.add(totalPatientTreatmentsPlan);
            pagePlan = new PageImpl<Treatment>(newTreatmentPlanList, pageable, pagePlan.getTotalElements());
        } else if (eventId != null) {
            log.debug("eventId part -> eventId:" + eventId + " patientId:" + patientId);
            page = treatmentRepository.findByEvent(eventId, PaginationUtil.generatePageRequest(offset, limit));
        } else {
            log.debug("last part -> eventId:" + eventId + " patientId:" + patientId);
            page = treatmentRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/treatments", offset, limit);
        return new ResponseEntity<>(new TreatmentDTO(page.getContent(), pagePlan==null?null:pagePlan.getContent()),
            headers, HttpStatus.OK);
    }

    /**
     * GET  /treatments -> get all the treatments.
     */
    @RequestMapping(value = "/benefits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Treatment>> getAll(@RequestParam(value = "firstDate", required = false) String firstDate,
                                                  @RequestParam(value = "lastDate", required = false) String lastDate,
                                                  @RequestParam(value = "doctorId", required = false) Long doctorId,
                                                  @RequestParam(value = "page", required = false) Integer offset,
                                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Treatment> page = null;
            Pageable pageable = PaginationUtil.generatePageRequest(offset, limit);
        DateTime firstDateTmp = FunctionsUtil.convertStringToDateTimeUTC(firstDate, WinDoctorConstants.WinDoctorPattern.DATE_PATTERN);
        DateTime lastDateTmp = FunctionsUtil.convertStringToDateTimeUTC(lastDate, WinDoctorConstants.WinDoctorPattern.DATE_PATTERN);
            page = treatmentRepository.findByFirstLastDatesDoctor(firstDateTmp, lastDateTmp.plusDays(1), SecurityUtils.getCurrerntStructure(), doctorId, pageable);
            if (Constants.FIRST_PAGE.equals(offset)) {
                Treatment totalPatientTreatments = treatmentRepository.findTotalByFirstLastDatesDoctor(firstDateTmp, lastDateTmp.plusDays(1), SecurityUtils.getCurrerntStructure(), doctorId);
                totalPatientTreatments.setId(-1l);
                log.info("Total paid price :" + totalPatientTreatments.getPrice());
                List<Treatment> newTreatmentList= new ArrayList<Treatment>(page.getContent());
                newTreatmentList.add(totalPatientTreatments);
                page = new PageImpl<Treatment>(newTreatmentList,pageable,page.getTotalElements());
            }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/benefits", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /treatments/:id -> get the "id" treatment.
     */
    @RequestMapping(value = "/treatments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Treatment> get(@PathVariable Long id) {
        log.debug("REST request to get Treatment : {}", id);
        return Optional.ofNullable(treatmentRepository.findOne(id))
            .map(treatment -> new ResponseEntity<>(
                treatment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /treatments/:id -> delete the "id" treatment.
     */
    @RequestMapping(value = "/treatments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Treatment : {}", id);
        Treatment treatment = treatmentRepository.findOne(id);
        if(Constants.STATUS_IN_PROGRESS.equals(treatment.getStatus().getId())){
            treatmentRepository.delete(id);
            treatmentSearchRepository.delete(id);
            if(treatment.getPlan()!=null){
                treatmentRepository.afterDeleteTreatmentSorting(treatment.getPlan().getId());
            }
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("treatment",
                id.toString()))
                .build();
        }else{
            return ResponseEntity.badRequest().headers(HeaderUtil.createEntityDeletionAlert("treatment",
                id.toString()))
                .build();
        }
    }

    /**
     * SEARCH  /_search/treatments/:query -> search for the treatment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/treatments/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Treatment>> search(@PathVariable String query, @RequestParam(value = "patientId", required = false) Long patientId, @RequestParam(value = "page", required = false) Integer offset,
                                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Treatment> page;
        page = treatmentRepository.findAllMatchString(Constants.PERCENTAGE + query + Constants.PERCENTAGE, patientId, PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/treatments", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    public void saveFundHistory(Treatment treatment, BigDecimal oldFundAmount) {
        if(!new Double(treatment.getFund().getAmount().doubleValue()).equals(oldFundAmount.doubleValue())){
            Fund_history fund_history = new Fund_history();
            fund_history.setFund(treatment.getFund());
            fund_history.setNew_amount(treatment.getFund().getAmount());
            fund_history.setOld_amount(oldFundAmount);
            fund_history.setAmount_movement(new BigDecimal(fund_history.getNew_amount().doubleValue() - fund_history.getOld_amount().doubleValue()));
            fund_history.setType_operation(fund_history.getAmount_movement().doubleValue() >= 0d ? true : false);
            fund_history.setTreatment(treatment);
            Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
            fund_history.setCreated_by(user.isPresent() ? user.get() : null);
            fund_history.setCreation_date(new DateTime());
            fund_historyRepository.save(fund_history);
        }
    }
}
