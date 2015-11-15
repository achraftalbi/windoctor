package com.winbit.windoctor.service;

import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.MailSetting;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.repository.MailSettingRepository;
import com.winbit.windoctor.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Session service management
 *
 * @author MBoufnichel
 */
@Service
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);

    @Inject
    private MailSettingRepository mailSettingRepository;

    public Long getCurrentStructure(){
        return SecurityUtils.getCurrerntStructure();
    }

    public  MailSetting getMailSetting(Long structureId){

        //TODO - get by structure id
        List<MailSetting> mailSettingList = mailSettingRepository.findAll();

        if(mailSettingList != null && mailSettingList.size() > 0){
            if(mailSettingList.size() > 1){
                log.error("More than one mail setting find for the current structure :"+ structureId);
            }
            return  mailSettingList.get(0);
        } else {
            log.error("No mail setting found for the current structure:"+ structureId);
            return null;
        }
    }
}
