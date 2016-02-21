package com.winbit.windoctor.service;

import com.winbit.windoctor.common.WinDoctorConstants;
import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.Event;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.EventRepository;
import com.winbit.windoctor.repository.StructureRepository;
import com.winbit.windoctor.repository.search.EventSearchRepository;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.service.util.DateUtil;
import com.winbit.windoctor.web.rest.util.FunctionsUtil;
import com.winbit.windoctor.web.rest.util.PaginationUtil;
import org.apache.commons.lang.CharEncoding;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private Environment env;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private EventSearchRepository eventSearchRepository;

    @Inject
    private SessionService sessionService;

    @Inject
    private StructureRepository structureRepository;

    /**
     * System default email address that sends the e-mails.
     */
    private InternetAddress from;

    @PostConstruct
    public void init() {
        try {
            from = new InternetAddress(env.getProperty("mail.from"));
        } catch (AddressException a) {
            log.error("AddressException internetAddress " + from + a);
        }
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("activationEmail", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
        log.debug("Sending password reset e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("passwordResetEmail", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendDoctorCreationAccountEmail(User user, String baseUrl) {
        log.debug("Sending Doctor account creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("doctorAccountCreationEmail", context);
        String subject = messageSource.getMessage("email.doctor.creation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEventCreationEmail(Event e) {
        User user = e.getUser();
        log.debug("Sending event creation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("eventdate", DateUtil.formatDate(e.getEvent_date()));
        context.setVariable("structure", e.getUser().getStructure());
        context.setVariable("baseUrl", getBaseUrlOnAsynchronousJobs());
        String content = templateEngine.process("eventCreationEmail", context);
        String subject = messageSource.getMessage("email.event.creation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEventCanceledEmail(Event e) {
        User user = e.getUser();
        log.debug("Sending event cancelation e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("eventdate", DateUtil.formatDate(e.getEvent_date()));
        context.setVariable("structure", e.getUser().getStructure());
        context.setVariable("baseUrl", getBaseUrlOnAsynchronousJobs());
        String content = templateEngine.process("eventCancelationEmail", context);
        String subject = messageSource.getMessage("email.event.cancelation.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEventBeforeRemindingEmail(Event e) {
        User user = e.getUser();
        log.debug("Sending event before reminder e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("eventdate", DateUtil.formatDate(e.getEvent_date()));
        context.setVariable("structure", e.getUser().getStructure());
        context.setVariable("baseUrl", getBaseUrlOnAsynchronousJobs());
        String content = templateEngine.process("remaidingBeforeEventMail", context);
        String subject = messageSource.getMessage("email.event.reminding.before.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEventAfterRemindingEmail(Event e) {
        User user = e.getUser();
        log.debug("Sending event after reminder e-mail to '{}'", user.getEmail());
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("eventdate", DateUtil.getFormattedTime(e.getEvent_date()));
        context.setVariable("structure", e.getUser().getStructure());
        context.setVariable("baseUrl", getBaseUrlOnAsynchronousJobs());
        String content = templateEngine.process("remaidingAfterEventMail", context);
        String subject = messageSource.getMessage("email.event.reminding.after.title", null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    private String getBaseUrlOnAsynchronousJobs() {
        return env.getProperty("email.hostname") + ":" + env.getProperty("server.port");
    }

    /************************************************************************************************************/
    /************************************************************************************************************/
    /***********************************          Sent mails treated                 ****************************/
    /************************************************************************************************************/
    /************************************************************************************************************/
    @Async
    public void sendPatientCreationAccountEmail(User user, Structure structure, String unencryptedPassword) {
        if (structure != null) {
            //MailSetting ms = sessionService.getMailSetting(structure.getId(), WinDoctorConstants.Mail.DOCTOR_CREATION_EMAIL_TYPE);
            //if(ms != null && Boolean.TRUE.equals(ms.getActivated())){
            log.debug("Sending Patient account creation e-mail to '{}'", user.getEmail());
            Locale locale = Locale.forLanguageTag(user.getLangKey());
            Context context = new Context(locale);
            context.setVariable("user", user);
            context.setVariable("structure", structure);
            context.setVariable("date", FunctionsUtil.convertDateToString(new Date(), WinDoctorConstants.WinDoctorPattern.DATE_PATTERN, locale));
            context.setVariable("baseUrl", getBaseUrlOnAsynchronousJobs());
            context.setVariable("unencryptedPassword", unencryptedPassword);
            String content = templateEngine.process("patientAccountCreationEmail", context);
            Object[] subjectVariables = {structure.getName()};
            String subject = messageSource.getMessage("email.patient.creation.title", subjectVariables, locale);
            try {
                from.setPersonal(structure.getName());
            } catch (UnsupportedEncodingException u) {
                log.error("UnsupportedEncodingException sendPatientCreationAccountEmail personnel :" + structure.getName() + u);
            }
            sendEmail(user.getEmail(), subject, content, false, true);
            //}
        }
    }

    @Async
    public void sendEventEmail(Event oldEvent, Event newEvent) {
        Event usedForTreatment = newEvent != null ? newEvent : oldEvent;
        log.debug("sendEventEmail e-mail to '{}'", usedForTreatment.getUser().getEmail());
        if (usedForTreatment.getUser().getStructure() != null) {
            log.debug("sendEventEmail e-mail to usedForTreatment.getUser().getStructure() != null '{}'", usedForTreatment.getUser().getEmail());
            Locale locale = Locale.forLanguageTag(usedForTreatment.getUser().getLangKey());
            Context context = new Context(locale);
            context.setVariable("user", usedForTreatment.getUser());
            context.setVariable("structure", usedForTreatment.getUser().getStructure());
            context.setVariable("date", FunctionsUtil.convertDateToString(new Date(), WinDoctorConstants.WinDoctorPattern.DATE_PATTERN, locale));
            Object[] subjectVariables = {usedForTreatment.getUser().getStructure().getName()};
            try {
                from.setPersonal(usedForTreatment.getUser().getStructure().getName());
            } catch (UnsupportedEncodingException u) {
                log.error("UnsupportedEncodingException sendAppointmentCreationEmail personnel :" + usedForTreatment.getUser().getStructure().getName() + u);
            }
            if (oldEvent == null) {
                log.debug("sendEventEmail e-mail to oldEvent == null '{}'", usedForTreatment.getUser().getEmail());
                if (newEvent != null &&
                    newEvent.getEventStatus() != null) {
                    log.debug("sendEventEmail e-mail to newEvent != null && newEvent.getEventStatus() != null '{}'", usedForTreatment.getUser().getEmail());
                    if (Constants.STATUS_IN_PROGRESS.equals(newEvent.getEventStatus().getId())) {
                        sendAppointmentCreationEmail(newEvent, context, subjectVariables, locale);
                    } else if (Constants.STATUS_REQUEST.equals(newEvent.getEventStatus().getId())) {
                        sendAppointmentRequestEmail(newEvent, context, subjectVariables, locale);
                    } else if (Constants.STATUS_BLOCKED.equals(newEvent.getEventStatus().getId())) {
                        sendAppointmentBlockedEmail(newEvent, context, subjectVariables, locale);
                    }
                }
            } else if (newEvent == null) {
                log.debug("sendEventEmail e-mail :new event null");
                if (oldEvent != null &&
                    oldEvent.getEventStatus() != null &&
                    Constants.STATUS_IN_PROGRESS.equals(oldEvent.getEventStatus().getId())) {
                    //The appointment was deleted and i send a message to the patient to inform him about this.
                    sendAppointmentCancellationEmail(oldEvent, context, subjectVariables, locale);
                }
            } else if (!oldEvent.getEventStatus().getId()
                .equals(newEvent.getEventStatus().getId())) {
                log.debug("sendEventEmail e-mail : old and new event not null");
                if (Constants.STATUS_CANCELLED.equals(newEvent.getEventStatus().getId())) {
                    //The appointment was cancelled.
                    sendAppointmentCancellationEmail(newEvent, context, subjectVariables, locale);
                } else if (Constants.STATUS_IN_PROGRESS.equals(newEvent.getEventStatus().getId())
                    && Constants.STATUS_REQUEST.equals(oldEvent.getEventStatus().getId())) {
                    //The Request for an appointment was approuved.
                    sendAppointmentAcceptanceEmail(newEvent, context, subjectVariables, locale);
                } else if (Constants.STATUS_REJECTED.equals(newEvent.getEventStatus().getId())
                    && Constants.STATUS_REQUEST.equals(oldEvent.getEventStatus().getId())) {
                    //The Request for an appointment was rejected.
                    sendAppointmentRejectEmail(newEvent, context, subjectVariables, locale);
                }
            }
        }
    }

    @Async
    public void sendAppointmentBlockedEmail(Event event, Context context, Object[] subjectVariables, Locale locale) {
        log.debug("Sending block apppointment correct treatment to '{}'", event.getUser().getEmail());
        List<Event> eventList = eventRepository.findEventsToBlock(event.getEvent_date(), event.getEvent_date().plusDays(1), event.getUser().getStructure().getId());
        log.info("list of events to block " + eventList);
        DateTime dateTime = new DateTime().withTime(0, 0, 0, 0);
        log.info("current date test " + dateTime + "");
        for (Event eventVar : eventList) {
            context.setVariable("user", eventVar.getUser());
            if (Constants.STATUS_IN_PROGRESS.
                equals(eventVar.getEventStatus().getId())) {
                eventVar.getEventStatus().setId(Constants.STATUS_CANCELLED);
                Event result = eventRepository.save(eventVar);
                eventSearchRepository.save(result);
                sendAppointmentCancellationEmail(result, context, subjectVariables, locale);
            } else if (Constants.STATUS_REQUEST.
                equals(eventVar.getEventStatus().getId())) {
                eventVar.getEventStatus().setId(Constants.STATUS_REJECTED);
                Event result = eventRepository.save(eventVar);
                eventSearchRepository.save(result);
                sendAppointmentRejectEmail(result, context, subjectVariables, locale);
            }
        }
    }

    @Async
    public void sendAppointmentJobRecall() {
        log.debug("Sending recall apppointment correct treatment to '{}'");
        DateTime dateTime = new DateTime().withTime(0,0,0,0);
        List<Event> eventList = eventRepository.
            findAppointmentsToRecall(dateTime.plusDays(1), dateTime.plusDays(3));
        log.info("list of events to recall " + eventList);
        for (Event eventVar : eventList) {
            Locale locale = Locale.forLanguageTag(eventVar.getUser().getLangKey());
            Context context = new Context(locale);
            context.setVariable("user", eventVar.getUser());
            context.setVariable("structure", eventVar.getUser().getStructure());
            context.setVariable("date", FunctionsUtil.convertDateToString(new Date(), WinDoctorConstants.WinDoctorPattern.DATE_PATTERN, locale));
            Object[] subjectVariables = {eventVar.getUser().getStructure().getName()};
            try {
                from.setPersonal(eventVar.getUser().getStructure().getName());
            } catch (UnsupportedEncodingException u) {
                log.error("UnsupportedEncodingException sendAppointmentCreationEmail personnel :" + eventVar.getUser().getStructure().getName() + u);
            }
            sendAppointmentRecallEmail(eventVar, context, subjectVariables, locale);
        }
    }

    @Async
    public void sendAppointmentCreationEmail(Event event, Context context, Object[] subjectVariables, Locale locale) {
        log.debug("Sending Patient appointment creation e-mail to '{}'", event.getUser().getEmail());
        context.setVariable("appointmentDate", FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.FULL_DATE_PATTERN, locale));
        String content = templateEngine.process("appointmentCreationEmail", context);
        String subject = messageSource.getMessage("email.appointment.creation.title", subjectVariables, locale);
        sendEmail(event.getUser().getEmail(), subject, content, false, true);
    }

    @Async
    public void sendAppointmentCancellationEmail(Event event, Context context, Object[] subjectVariables, Locale locale) {
        log.debug("Sending Patient appointment cancellation e-mail to '{}'", event.getUser().getEmail());
        context.setVariable("appointmentDate", FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.FULL_DATE_PATTERN, locale));
        String content = templateEngine.process("appointmentCancellationEmail", context);
        String subject = messageSource.getMessage("email.appointment.cancellation.title", subjectVariables, locale);
        sendEmail(event.getUser().getEmail(), subject, content, false, true);
    }

    @Async
    public void sendAppointmentRequestEmail(Event event, Context context, Object[] subjectVariables, Locale locale) {
        log.debug("Sending Patient appointment request e-mail to '{}'", event.getUser().getEmail());
        context.setVariable("appointmentDate", FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.FULL_DATE_PATTERN, locale));
        String content = templateEngine.process("appointmentRequestEmail", context);
        String subject = messageSource.getMessage("email.appointment.request.title", subjectVariables, locale);
        sendEmail(event.getUser().getEmail(), subject, content, false, true);
    }

    @Async
    public void sendAppointmentAcceptanceEmail(Event event, Context context, Object[] subjectVariables, Locale locale) {
        log.debug("Sending Patient appointment acceptance e-mail to '{}'", event.getUser().getEmail());
        context.setVariable("appointmentDate", FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.FULL_DATE_PATTERN, locale));
        String content = templateEngine.process("appointmentRequestAcceptanceEmail", context);
        String subject = messageSource.getMessage("email.appointment.acceptance.title", subjectVariables, locale);
        sendEmail(event.getUser().getEmail(), subject, content, false, true);
    }

    @Async
    public void sendAppointmentRejectEmail(Event event, Context context, Object[] subjectVariables, Locale locale) {
        log.debug("Sending Patient appointment reject e-mail to '{}'", event.getUser().getEmail());
        context.setVariable("appointmentDate", FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.FULL_DATE_PATTERN, locale));
        String content = templateEngine.process("appointmentRequestRejectEmail", context);
        String subject = messageSource.getMessage("email.appointment.reject.title", subjectVariables, locale);
        sendEmail(event.getUser().getEmail(), subject, content, false, true);
    }

    @Async
    public void sendAppointmentRecallEmail(Event event, Context context, Object[] subjectVariables, Locale locale) {
        log.debug("Sending Patient appointment recall e-mail to '{}'", event.getUser().getEmail());
        context.setVariable("appointmentDate", FunctionsUtil.convertDateToString(event.getEvent_date().toDate(), Constants.FULL_DATE_PATTERN, locale));
        String content = templateEngine.process("appointmentRecallEmail", context);
        String subject = messageSource.getMessage("email.appointment.recall.title", subjectVariables, locale);
        sendEmail(event.getUser().getEmail(), subject, content, false, true);
    }


}
