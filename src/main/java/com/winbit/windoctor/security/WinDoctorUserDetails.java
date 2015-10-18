package com.winbit.windoctor.security;

import com.winbit.windoctor.domain.Structure;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * WinDoctor user details
 *
 * @author MBoufnichel
 */
public class WinDoctorUserDetails extends User {

    private Long structureId;
    private String login;

    public WinDoctorUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,Long structureId, String login) {
        super(username, password, true, true, true, true, authorities);
        this.structureId = structureId;
        this.login = login;
    }

    public Long getStructureId() {
        return structureId;
    }

    public String getLogin() {
        return login;
    }
}
