package com.winbit.windoctor.service;

import com.winbit.windoctor.security.SecurityUtils;
import com.winbit.windoctor.security.WinDoctorUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Session service management
 *
 * @author MBoufnichel
 */
@Service
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);

    public Long getCurrentStructure(){
        return SecurityUtils.getCurrerntStructure();
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
