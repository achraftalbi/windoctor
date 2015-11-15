package com.winbit.windoctor.service;

import com.winbit.windoctor.domain.MailSetting;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.MailSettingRepository;
import com.winbit.windoctor.repository.StructureRepository;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailSettingService {

    private final Logger log = LoggerFactory.getLogger(MailSettingService.class);

    @Inject
    private MailSettingRepository mailSettingRepository;

    @Inject
    private SessionService sessionService;

    public Page<MailSetting> findAll(Pageable var1){
        Long currentStructureId = sessionService.getCurrentStructure();
        if(currentStructureId != null){
            return mailSettingRepository.findAll(currentStructureId, var1);
        }
        return mailSettingRepository.findAll(var1);
    }

}
