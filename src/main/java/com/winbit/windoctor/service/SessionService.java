package com.winbit.windoctor.service;

import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.MailSetting;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.repository.MailSettingRepository;
import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.security.WinDoctorUserDetails;
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

    public  MailSetting getMailSetting(Long structureId, Long emailType){
        return mailSettingRepository.findOne(structureId, emailType);
    }

    public boolean isAdmin(){
        WinDoctorUserDetails current = SecurityUtils.getCurrentWinDoctorUserDetails();
        if(current!=null){
            return current.isAdmin();
        }
        return false;
    }

    public boolean isDoctor(){
        WinDoctorUserDetails current = SecurityUtils.getCurrentWinDoctorUserDetails();
        if(current!=null){
            return current.isDoctor();
        }
        return false;
    }

    public boolean isAssistant(){
        WinDoctorUserDetails current = SecurityUtils.getCurrentWinDoctorUserDetails();
        if(current!=null){
            return current.isAssistant();
        }
        return false;
    }

    public boolean isPatient(){
        WinDoctorUserDetails current = SecurityUtils.getCurrentWinDoctorUserDetails();
        if(current!=null){
            return current.isPatient();
        }
        return false;
    }


}
