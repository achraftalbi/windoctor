package com.winbit.windoctor.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown is the user hasn't any role
 *
 * @author MBoufnichel
 */
public class UserWithoutRoleException extends AuthenticationException {

    public UserWithoutRoleException(String message) {
        super(message);
    }

    public UserWithoutRoleException(String message, Throwable t) {
        super(message, t);
    }
}
