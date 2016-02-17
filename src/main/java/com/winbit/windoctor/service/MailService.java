package com.winbit.windoctor.service;

import com.winbit.windoctor.common.WinDoctorConstants;
import com.winbit.windoctor.domain.Event;
import com.winbit.windoctor.domain.MailSetting;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.StructureRepository;
import com.winbit.windoctor.service.util.DateUtil;
import com.winbit.windoctor.web.rest.util.FunctionsUtil;
import org.apache.commons.lang.CharEncoding;
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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private SessionService sessionService;

    @Inject
    private StructureRepository structureRepository;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @PostConstruct
    public void init() {
        this.from = env.getProperty("mail.from");
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

    private String getBaseUrlOnAsynchronousJobs(){
        return env.getProperty("email.hostname") +":"+ env.getProperty("server.port");
    }

    /************************************************************************************************************/
    /************************************************************************************************************/
    /***********************************          Sent mails treated                 ****************************/
    /************************************************************************************************************/
    /************************************************************************************************************/
    @Async
    public void sendPatientCreationAccountEmail(User user,Structure structure) {
        if(structure != null){
            //MailSetting ms = sessionService.getMailSetting(structure.getId(), WinDoctorConstants.Mail.DOCTOR_CREATION_EMAIL_TYPE);
            //if(ms != null && Boolean.TRUE.equals(ms.getActivated())){
                log.debug("Sending Patient account creation e-mail to '{}'", user.getEmail());
                Locale locale = Locale.forLanguageTag(user.getLangKey());
                Context context = new Context(locale);
                context.setVariable("user", user);
                context.setVariable("structure", structure);
                context.setVariable("date", FunctionsUtil.convertDateToString(new Date(),WinDoctorConstants.WinDoctorPattern.DATE_PATTERN));
                context.setVariable("baseUrl", getBaseUrlOnAsynchronousJobs());
                String content = templateEngine.process("patientAccountCreationEmail", context);
                Object[] subjectVariables = {structure.getName()};
                String subject = messageSource.getMessage("email.patient.creation.title", subjectVariables, locale);
                sendEmail(user.getEmail(), subject, content, false, true);
            //}
        }
    }



}
