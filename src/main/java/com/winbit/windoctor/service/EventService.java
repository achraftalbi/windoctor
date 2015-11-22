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

    @Inject
    private MailService mailService;

    public Event save(Event event){
        event.setCreation_date(new DateTime());
        event.setCreationMailSent(Boolean.FALSE);
        Status status = statusRepository.findOneById(WinDoctorConstants.EventStatus.EVENT_CREATION);
        event.setEventStatus(status);
        return eventRepository.save(event);
    }



    @Scheduled(cron = "*/10 * * * * *")
//    @Scheduled(cron = "* */5 * * * *")
    public void notifyEventCreationEmail() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        DateTime startDate = new DateTime(c.getTime());

        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND, 59);
        DateTime endDate = new DateTime(c.getTime());

        List<Event> events = eventRepository.getAllNewelyCreatedEvent(
            WinDoctorConstants.Mail.EVENT_CREATION_EMAIL_TYPE,
            startDate,
            endDate,
            WinDoctorConstants.EventStatus.EVENT_CREATION);

        if(CollectionUtils.isNotEmpty(events)){
            log.info(events.size()+" new consultation created !");
            log.debug("Start emailing patients...");
            events
                .stream()
                .filter(event -> event.getUser()!=null)
                .forEach(
                    event ->  {
                        mailService.sendEventCreationEmail(event);
                        event.setCreationMailSent(Boolean.TRUE);
                        eventRepository.save(event);
                    }

                );
        }
    }
}
