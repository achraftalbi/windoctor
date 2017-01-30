package com.winbit.windoctor.security;

import com.winbit.windoctor.domain.Authority;
import com.winbit.windoctor.domain.User;
import com.winbit.windoctor.repository.AuthorityRepository;
import com.winbit.windoctor.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();
        Optional<User> userFromDatabase =  userRepository.findOneByLogin(lowercaseLogin);
        return userFromDatabase.map(user -> {
            /*if (!user.getActivated()) {
                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
            }*/
            if(user.getAuthorities() == null && user.getAuthorities().isEmpty()){
                throw new UserWithoutRoleException("User " + lowercaseLogin + " has no role, please fix the configuration issue, normaly a user must have a role!");
            }
            List<Authority> authorities = authorityRepository.findAllUnderPriority(user.getAuthorities().iterator().next().getPriority());
            List<GrantedAuthority> grantedAuthorities = authorities.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
            return new WinDoctorUserDetails(lowercaseLogin,
                    user.getPassword(),
                    grantedAuthorities, (user.getStructure()!=null)?user.getStructure().getId():null, user.getLogin());
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }
}
