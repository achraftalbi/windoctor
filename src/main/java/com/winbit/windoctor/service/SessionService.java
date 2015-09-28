package com.winbit.windoctor.service;

import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.domain.Structure;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * Session service management
 *
 * @author MBoufnichel
 */
@Service
public class SessionService {

    public Structure getCurrentStructure(HttpSession session){
        return (Structure) session.getAttribute(Constants.CURRENT_STRUCTURE);
    }

    public void setCurrentStructure(Structure structure, HttpSession session){
        session.setAttribute(Constants.CURRENT_STRUCTURE, structure);
    }

    public void clearCurrentStructure(HttpSession session){
        if(hasCurrentSession(session)){
            session.removeAttribute(Constants.CURRENT_STRUCTURE);
        }
    }

    public boolean hasCurrentSession(HttpSession session){
        return session.getAttribute(Constants.CURRENT_STRUCTURE) != null;
    }
}
