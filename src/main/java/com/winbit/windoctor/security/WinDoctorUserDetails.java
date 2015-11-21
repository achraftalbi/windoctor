package com.winbit.windoctor.security;

import com.winbit.windoctor.domain.Structure;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private boolean admin = false;
    private boolean doctor = false;
    private boolean assistant = false;
    private boolean patient = false;

    public WinDoctorUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,Long structureId, String login) {
        super(username, password, true, true, true, true, authorities);
        this.structureId = structureId;
        this.login = login;
        setIdentity(authorities);

    }

    public Long getStructureId() {
        return structureId;
    }

    public String getLogin() {
        return login;
    }

    public boolean isAdmin() {
        return admin;
    }

    private void setIdentity(Collection<? extends GrantedAuthority> authorities){
        if(CollectionUtils.isNotEmpty(authorities)){

            if(containsRole(authorities, AuthoritiesConstants.ADMIN)){
                admin = true;
            } else if(containsRole(authorities, AuthoritiesConstants.DOCTOR)){
                doctor = true;
            } else if(containsRole(authorities,AuthoritiesConstants.ASSISTANT)){
                assistant = true;
            }else if(containsRole(authorities,AuthoritiesConstants.PATIENT)){
                patient = true;
            }
        }

    }

    private boolean containsRole(Collection<? extends GrantedAuthority> authorities, String role){
        for(GrantedAuthority ga: authorities){
            if (role.equals(ga.getAuthority())){
                return true;
            }
        }
        return false;
    }

    public boolean isDoctor() {
        return doctor;
    }

    public boolean isAssistant() {
        return assistant;
    }

    public boolean isPatient() {
        return patient;
    }
}
