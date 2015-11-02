package com.winbit.windoctor.service;

import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.Structure;
import com.winbit.windoctor.security.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * Session service management
 *
 * @author MBoufnichel
 */
@Service
public class SessionService {

    public Long getCurrentStructure(){
        return SecurityUtils.getCurrerntStructure();
    }
}
