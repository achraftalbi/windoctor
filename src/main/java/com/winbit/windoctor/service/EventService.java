package com.winbit.windoctor.service;

import com.winbit.windoctor.common.WinDoctorConstants;
import com.winbit.windoctor.domain.Event;
import com.winbit.windoctor.domain.Status;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.EventRepository;
import com.winbit.windoctor.repository.StatusRepository;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Boufnichel on 18/11/2015.
 */
@Service
public class EventService {

    private final Logger log = LoggerFactory.getLogger(EventService.class);

    @Inject
    private EventRepository eventRepository;

    @Inject
    private StatusRepository statusRepository;

    public Event save(Event event){
        event.setCreation_date(new DateTime());

        event.setCreationMailSent(Boolean.FALSE);
        event.setCanceledMailSent(Boolean.FALSE);
        event.setRemindAfterMail(Boolean.FALSE);
        event.setRemindBeforeMail(Boolean.FALSE);

        Status status = statusRepository.findOneById(WinDoctorConstants.EventStatus.EVENT_CREATION);
        event.setEventStatus(status);

        return eventRepository.save(event);
    }
}
