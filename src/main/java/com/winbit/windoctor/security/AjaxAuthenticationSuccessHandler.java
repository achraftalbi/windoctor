package com.winbit.windoctor.security;

import com.winbit.windoctor.config.Constants;
import com.winbit.windoctor.service.SessionService;
import com.winbit.windoctor.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Inject
    SessionService sessionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        WinDoctorUserDetails user = (WinDoctorUserDetails)authentication.getPrincipal();
        if(user.getStructureId() != null){
            sessionService.setCurrentStructure(user.getStructureId(), request.getSession());
            sessionService.setCurrentUserLogin(user.getLogin(), request.getSession());
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
