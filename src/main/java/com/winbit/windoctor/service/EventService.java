package com.winbit.windoctor.service;

import com.winbit.windoctor.common.WinDoctorConstants;
import com.winbit.windoctor.domain.Event;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.EventRepository;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Boufnichel on 18/11/2015.
 */
@Service
public class EventService {

    @Inject
    EventRepository eventRepository;

    public Event save(Event e){
        e.setCreation_date(new DateTime());
        e.setCreationMailSent(Boolean.FALSE);
        return eventRepository.save(e);
    }

    @Scheduled(cron = "*/5 * * * * *")
//    @Scheduled(cron = "* */5 * * * *")
    public void notifyEventCreationEmail() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        Date startDate = c.getTime();

        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND, 59);
        Date endDate = c.getTime();
        List<Event> events = eventRepository.getAllNewelyCreatedEvent(WinDoctorConstants.Mail.EVENT_CREATION_EMAIL_TYPE);
        System.out.printf(""+events.size());

    }
}
