package com.winbit.windoctor.job;

import com.winbit.windoctor.common.WinDoctorConstants;
import com.winbit.windoctor.domain.Event;
import com.winbit.windoctor.repository.EventRepository;
import com.winbit.windoctor.repository.StatusRepository;
import com.winbit.windoctor.service.MailService;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;

/**
 * Created by boufnichel on 22/11/2015.
 */

@Component
public final class EventJobs {

    private final Logger log = LoggerFactory.getLogger(EventJobs.class);

    @Inject
    private EventRepository eventRepository;

    @Inject
    private MailService mailService;

    @Inject
    private Environment env;

    //@Scheduled(cron = "*/10 * * * * *")
    /*public void notifyEventCreationEmailJob() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        DateTime startDate = new DateTime(c.getTime());

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        DateTime endDate = new DateTime(c.getTime());

        List<Event> events = eventRepository.getAllNewelyCreatedEvents(
            WinDoctorConstants.Mail.EVENT_CREATION_EMAIL_TYPE,
            startDate,
            endDate,
            WinDoctorConstants.EventStatus.EVENT_CREATION);

        if (CollectionUtils.isNotEmpty(events)) {
            log.info("Creation event job> Start...");
            log.info("Creation event job > "+events.size() + " new consultation created !");
            log.info("Creation event job> emailing patients...");
            events
                .stream()
                .filter(event -> event.getUser() != null)
                .forEach(
                    event -> {
                        mailService.sendEventCreationEmail(event);
                        event.setCreationMailSent(Boolean.TRUE);
                        eventRepository.save(event);
                    }

                );
        }
    }*/

    //@Scheduled(cron = "*/5 * * * * *")
    /*public void notifyEventCanceledEmailJob() {
        List<Event> events = eventRepository.getAllCanceledEvents(
            WinDoctorConstants.Mail.EVENT_CANCELATION_EMAIL_TYPE,
            WinDoctorConstants.EventStatus.EVENT_CANCELATION);

        if (CollectionUtils.isNotEmpty(events)) {
            log.info("Cancelation event job > Start...");
            log.info("Cancelation event job > "+events.size() + " consultation canceled !");
            log.info("Cancelation event job > emailing patients...");
            events
                .stream()
                .filter(event -> event.getUser() != null)
                .forEach(
                    event -> {
                        mailService.sendEventCanceledEmail(event);
                        event.setCanceledMailSent(Boolean.TRUE);
                        eventRepository.save(event);
                    }

                );
        }
    }

    //@Scheduled(cron = "* * /5 * * * *")
    @Scheduled(cron = "* * * * * *")
    public void reminderBeforeEventEmailJob() {

        String beforeDelai =  env.getRequiredProperty("email.reminder.before");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, Integer.valueOf(beforeDelai));

        List<Event> events = eventRepository.getAllEventBeforeReminding(
            WinDoctorConstants.Mail.BEFORE_EVENT_START_EMAIL_TYPE,
            WinDoctorConstants.EventStatus.EVENT_CREATION, new DateTime(c.getTime()));

        if (CollectionUtils.isNotEmpty(events)) {
            log.info("Reminding before event job > Start...");
            log.info("Reminding before event job > "+events.size() + " consultation to remind !");
            log.info("Reminding before event job > emailing patients...");

            events
                .stream()
                .filter(event -> event.getUser() != null)
                .forEach(
                    event -> {
                        mailService.sendEventBeforeRemindingEmail(event);
                        event.setRemindBeforeMail(Boolean.TRUE);
                        eventRepository.save(event);
                    }

                );
        }
    }

    //@Scheduled(cron = "* * /1 * * * *")
    @Scheduled(cron = "* * * * * *")
    public void reminderAfterEventEmailJob() {

        String afterDelai =  env.getRequiredProperty("email.reminder.after");

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, Integer.valueOf(Integer.valueOf(afterDelai).intValue() * -1));

        List<Event> events = eventRepository.getAllEventAfterReminding(
            WinDoctorConstants.Mail.AFTER_EVENT_START_EMAIL_TYPE,
            WinDoctorConstants.EventStatus.EVENT_CREATION, new DateTime(c.getTime()));

        if (CollectionUtils.isNotEmpty(events)) {
            log.info("Reminding after event job > Start...");
            log.info("Reminding after event job > "+events.size() + " consultation to remind !");
            log.info("Reminding after event job > emailing patients...");

            events
                .stream()
                .filter(event -> event.getUser() != null)
                .forEach(
                    event -> {
                        System.out.println("Event date="+ event.getEvent_date());
                        mailService.sendEventAfterRemindingEmail(event);
                        event.setRemindAfterMail(Boolean.TRUE);
                        eventRepository.save(event);
                    }

                );
        }
    }*/

    @Scheduled(cron = "0 0 1 * * *")
    public void appointmentsRecallEmailJob() {
        mailService.sendAppointmentJobRecall();
    }

}
