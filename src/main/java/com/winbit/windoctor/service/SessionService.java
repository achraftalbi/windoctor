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

    public void setCurrentStructure(Long structure, HttpSession session){
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

    public Structure getCurrentUserLogin(HttpSession session){
        return (Structure) session.getAttribute(Constants.CURRENT_USER_LOGIN);
    }

    public void setCurrentUserLogin(String login, HttpSession session){
        session.setAttribute(Constants.CURRENT_USER_LOGIN, login);
    }

    public void clearCurrentUserLogin(HttpSession session){
        if(hasCurrentUserLogin(session)){
            session.removeAttribute(Constants.CURRENT_USER_LOGIN);
        }
    }

    public boolean hasCurrentUserLogin(HttpSession session){
        return session.getAttribute(Constants.CURRENT_USER_LOGIN) != null;
    }
}
