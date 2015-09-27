package com.winbit.windoctor.security;

import com.winbit.windoctor.domain.Structure;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * TODO - Donne moi une petite description stp 8)
 *
 * @author MBoufnichel
 */
public class WinDoctorUserDetails extends User {

    private Structure structure;

    public WinDoctorUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,Structure structure) {
        super(username, password, true, true, true, true, authorities);
        this.structure = structure;
    }


    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}
