package com.winbit.windoctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.winbit.windoctor.common.WinDoctorConstants;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.Event;
import com.winbit.windoctor.repository.EventRepository;
import com.winbit.windoctor.repository.UserRepository;
import com.winbit.windoctor.repository.search.EventSearchRepository;
import com.winbit.windoctor.service.EventService;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.service.MailService;
import com.winbit.windoctor.web.rest.dto.EventDTO;
import com.winbit.windoctor.web.rest.util.FunctionsUtil;
import com.winbit.windoctor.web.rest.util.HeaderUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.text.SimpleDateFormat;
import java.util.*;

import com.winbit.windoctor.web.rest.util.PaginationUtil;
import org.springframework.data.domain.Page;

/**
 * REST controller for managing Event.
 */
@RestController
@RequestMapping("/api")
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    @Inject
    private EventRepository eventRepository;

    @Inject
    private EventSearchRepository eventSearchRepository;

    @Inject
    private EventService eventService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    /**
     * POST  /events -> Create a new event.
     */
    @RequestMapping(value = "/events",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Event> create(@Valid @RequestBody Event event) throws URISyntaxException {
        log.debug("REST request to save Event : {}", event);
        if (event.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new event cannot already have an ID").body(null);
        }
        log.debug("new event status getId " + event.getEventStatus().getId());
        Event result = eventService.save(event);
        eventSearchRepository.save(result);
        ResponseEntity<Event> responseEntity;
        if(event.getEventStatus().getId()!=null){
            if(Constants.STATUS_BLOCKED.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("event.blockDays",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_DATE_FORMAT)))
                    .body(result);
            } else if(Constants.STATUS_IN_PROGRESS.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("calendar.appointment",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .body(result);
            } else if(Constants.STATUS_REQUEST.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("calendar.request",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .body(result);
            } else if(Constants.STATUS_VISIT.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("calendar.visit",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .body(result);
            }else{
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("event", result.getId().toString()))
                    .body(result);
            }
        }else {
            responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("event", result.getId().toString()))
                .body(result);
        }
        log.info("user event "+result.getUser()+" user id "+result.getUser().getId());
        result.setUser(userRepository.findOne(result.getUser().getId()));
        mailService.sendEventEmail(null,result);
        return responseEntity;
    }

    /**
     * PUT  /events -> Updates an existing event.
     */
    @RequestMapping(value = "/events",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Event> update(@Valid @RequestBody Event event) throws URISyntaxException {
        log.debug("REST request to update Event : {}", event);
        if (event.getId() == null) {
            return create(event);
        }
        Event oldEvent = eventRepository.findOne(event.getId());
        Event result = eventRepository.save(event);
        eventSearchRepository.save(event);
        ResponseEntity<Event> responseEntity;

        if(event.getEventStatus().getId()!=null){
            if(Constants.STATUS_BLOCKED.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityUpdateAlert("event.blockDays",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_DATE_FORMAT)))
                    .body(result);
            } else if(Constants.STATUS_IN_PROGRESS.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityUpdateAlert("calendar.appointment",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .body(result);
            } else if(Constants.STATUS_REQUEST.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityUpdateAlert("calendar.request",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .body(result);
            } else if(Constants.STATUS_VISIT.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityUpdateAlert("calendar.visit",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .body(result);
            } else if(Constants.STATUS_CANCELLED.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityUpdateAlert("calendar.appointment.annuled",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .body(result);
            } else if(Constants.STATUS_CANCELLED_BY_PATIENT.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityUpdateAlert("calendar.appointment.annuledByPatient",
                        result.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(result.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .body(result);
            }else{
                responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                    .headers(HeaderUtil.createEntityUpdateAlert("event", result.getId().toString()))
                    .body(result);
            }
        }else {
            responseEntity = ResponseEntity.created(new URI("/api/events/" + result.getId()))
                .headers(HeaderUtil.createEntityUpdateAlert("event", result.getId().toString()))
                .body(result);
        }
        mailService.sendEventEmail(oldEvent, event);

        return responseEntity;
    }


    /**
     * GET  /event_reasons -> get all the event_reasons.
     */
    @RequestMapping(value = "/eventsAll",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Event> getAll() {
        log.debug("REST request to get all Event");
        List<Event> listEventsFounded = eventRepository.findAllEvents(SecurityUtils.getCurrerntStructure());
        List<Event> listEvents = new ArrayList<Event>();
        log.info("TimeZone test URI " + TimeZone.getDefault().getID());
        for (Event event : listEventsFounded) {
            Event eventTmp = new Event();
            eventTmp.setId(event.getId());
            eventTmp.setEventReason(event.getEventReason());
            eventTmp.setEventStatus(event.getEventStatus());
            eventTmp.setEvent_date(event.getEvent_date().withZoneRetainFields(DateTimeZone.forID(TimeZone.getDefault().getID())));
            listEvents.add(eventTmp);
            log.info("New events event_date change " + event.getEvent_date());
        }
        return listEvents;
    }

    /**
     * GET  /eventsNotification -> get all the events notification for the current structure.
     */
    @RequestMapping(value = "/eventsNotification",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Event>> getAllNotification(@RequestParam(value = "page", required = false) Integer offset,
                                                          @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get events page per_page");
        Page<Event> page = null;

        page = eventRepository.getAllNotification(SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/eventsNotification", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /events -> get all the events.
     */
    @RequestMapping(value = "/events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Event>> getAll(@RequestParam(value = "selectedDate", required = false) String selectedDate, @RequestParam(value = "page", required = false) Integer offset,
                                              @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get events page per_page");
        Page<Event> page = null;
        log.debug("selectedDate " + selectedDate);
        if (selectedDate == null) {
            log.debug("selectedDate 1 " + selectedDate);
            page = eventRepository.findAll(SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
        } else {
            selectedDate = selectedDate.split("GMT")[0].trim();
            DateTime dateTimeInUTC = FunctionsUtil.convertStringToDateTimeUTC(selectedDate, WinDoctorConstants.WinDoctorPattern.DATE_PATTERN_BROWZER);
            log.debug("first date:" + dateTimeInUTC + " segond date:" + dateTimeInUTC.plusDays(1));
            page = eventRepository.findAll(dateTimeInUTC, dateTimeInUTC.plusDays(1), SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
            for (Event event : page) {
                log.debug("event founded " + event.getEvent_date());
            }
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/events", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /events -> get all the events.
     */
    @RequestMapping(value = "/eventsBlock",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Event>> getAllEventsBlock(@RequestParam(value = "statusType", required = false) Long statusType, @RequestParam(value = "page", required = false) Integer offset,
                                                         @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get eventsBlock page per_page");
        Page<Event> page = null;
        log.debug("statusType " + statusType);

        page = eventRepository.findAllEventsBlock(statusType, SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/events", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/eventDTO",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EventDTO> getAllEventDTO(@RequestParam(value = "selectedDate", required = false) String selectedDate,
                                                   @RequestParam(value = "eventId", required = false) Long eventId)
        throws URISyntaxException {
        log.debug("REST request to get eventDTO page per_page");
        EventDTO eventDTO = new EventDTO();
        List<Event> eventList = null;
        log.debug("selectedDate " + selectedDate);
        log.debug("eventId " + eventId);
        if (selectedDate == null) {
            log.debug("selectedDate 1 " + selectedDate);
            eventList = eventRepository.findAllEvents(SecurityUtils.getCurrerntStructure());
            eventDTO.setEventList(eventList);
        } else {
            log.debug("selectedDate 222 " + selectedDate);
            selectedDate = selectedDate.split("GMT")[0].trim();
            DateTime dateTimeInUTC = FunctionsUtil.convertStringToDateTimeUTC(selectedDate, WinDoctorConstants.WinDoctorPattern.DATE_PATTERN_BROWZER);
            eventList = eventRepository.findAll(dateTimeInUTC, dateTimeInUTC.plusDays(1), SecurityUtils.getCurrerntStructure());
            eventDTO.setEventList(eventList);
            Long startHour = 6l;
            Long endHour = 24l;
            eventDTO.setStartDateList(new ArrayList<String>());
            for (Long counter = startHour; counter < endHour; counter++) {
                String hour = (counter < 10l ? "0" : "") + counter;
                eventDTO.getStartDateList().add(hour + ":00");
                eventDTO.getStartDateList().add(hour + ":15");
                eventDTO.getStartDateList().add(hour + ":30");
                eventDTO.getStartDateList().add(hour + ":45");
            }
            eventDTO.getStartDateList().add(endHour + ":00");
            eventDTO.setEndDateList(new ArrayList<String>(eventDTO.getStartDateList()));
            if (eventList != null && eventList.size() > 0) {
                SimpleDateFormat output = new SimpleDateFormat("HH:mm");
                output.setTimeZone(TimeZone.getTimeZone("UTC"));
                for (Event event : eventList) {
                    String formattedTimeStartDate = output.format(event.getEvent_date().toDate());
                    log.info("formattedTimeStartDate 1 " + formattedTimeStartDate);
                    log.info("formattedTimeStartDateUTC 1 " + event.getEvent_date());
                    String formattedTimeEndDate = output.format(event.getEvent_date_end().toDate());
                    log.info("formattedTimeEndDate 2 " + formattedTimeEndDate);
                    log.info("formattedTimeEndDate 2 " + event.getEvent_date_end());
                    boolean findStartDate = false;
                    if (!event.getId().equals(eventId)) {
                        for (int i = 0; i < eventDTO.getStartDateList().size(); i++) {
                            log.info("eventDTO.getStartDateList().get(" + i + ") --- " + eventDTO.getStartDateList().get(i));
                            if (formattedTimeStartDate.equals(eventDTO.getStartDateList().get(i)) && (!findStartDate)) {
                                findStartDate = true;
                                log.info("Start eventDTO.getStartDateList().get(" + i + ") --- " + eventDTO.getStartDateList().get(i));
                                eventDTO.getStartDateList().set(i, "NO");
                            } else if (formattedTimeEndDate.equals(eventDTO.getStartDateList().get(i)) && findStartDate) {
                                log.info("End eventDTO.getStartDateList().get(" + i + ") --- " + eventDTO.getStartDateList().get(i));
                                eventDTO.getEndDateList().set(i, "NO");
                                break;
                            } else if (findStartDate) {
                                log.info("elses eventDTO.getStartDateList().get(" + i + ") --- " + eventDTO.getStartDateList().get(i));
                                eventDTO.getStartDateList().set(i, "NO");
                                eventDTO.getEndDateList().set(i, "NO");
                            }
                        }
                    }
                }

            }
            if (eventDTO.getStartDateList().size() > 0 &&
                eventDTO.getStartDateList().get(eventDTO.getStartDateList().size() - 1).equals(endHour + ":00")) {
                eventDTO.getStartDateList().remove(eventDTO.getStartDateList().size() - 1);
            }
            log.info("eventDTO.setStartDateList " + eventDTO.getStartDateList());
        }

        return new ResponseEntity<>(
            eventDTO,
            HttpStatus.OK);
    }

    /**
     * GET  /events/:id -> get the "id" event.
     */
    @RequestMapping(value = "/events/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Event> get(@PathVariable Long id) {
        log.debug("REST request to get Event : {}", id);
        return Optional.ofNullable(eventRepository.findOne(id))
            .map(event -> new ResponseEntity<>(
                event,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * DELETE  /events/:id -> delete the "id" event.
     */
    @RequestMapping(value = "/events/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Event change : {}", id);
        Event event = eventRepository.findOne(id);
        eventRepository.delete(id);
        eventSearchRepository.delete(id);
        ResponseEntity<Void> responseEntity;
        if(event.getEventStatus().getId()!=null){
            if(Constants.STATUS_BLOCKED.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("event.blockDays",
                    event.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.GLOBAL_DATE_FORMAT)))
                    .build();
            } else if(Constants.STATUS_IN_PROGRESS.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("calendar.appointment",
                    event.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .build();
            } else if(Constants.STATUS_REQUEST.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("calendar.request",
                    event.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .build();
            } else if(Constants.STATUS_VISIT.equals(event.getEventStatus().getId())) {
                responseEntity = ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("calendar.visit",
                    event.getEvent_date() == null ? "" : FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.GLOBAL_HOUR_MINUTE)))
                    .build();
            }else{
                responseEntity = ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("event", id.toString())).build();
            }
        }else {
            responseEntity = ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("event", id.toString())).build();
        }
        mailService.sendEventEmail(event, null);

        return responseEntity;
    }

    /**
     * SEARCH  /_search/events/:query -> search for the event corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/events/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Event>> search(@PathVariable String query,@RequestParam(value = "selectedDate", required = false) String selectedDate
        , @RequestParam(value = "page", required = false) Integer offset,
                                              @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get events change page per_page");
        Page<Event> page = null;
        log.debug("REST request to get events query "+query);
        selectedDate = selectedDate.split("GMT")[0].trim();
        DateTime dateTimeInUTC = FunctionsUtil.convertStringToDateTimeUTC(selectedDate, WinDoctorConstants.WinDoctorPattern.DATE_PATTERN_BROWZER);
        page = eventSearchRepository.search(query,dateTimeInUTC, dateTimeInUTC.plusDays(1), SecurityUtils.getCurrerntStructure(), PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/events", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * SEARCH  /_search/eventsBlock/:query -> search for the event corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/eventsBlock/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Event>> searchBlock(@PathVariable String query, @RequestParam(value = "page", required = false) Integer offset,
                                                   @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        log.debug("REST request to get eventsBlock page per_page");
        Page<Event> page = null;
        log.debug("REST request to get eventsBlock query "+query);
        page = eventSearchRepository.search(query, Constants.STATUS_BLOCKED,SecurityUtils.getCurrerntStructure(),PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/eventsBlock", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
