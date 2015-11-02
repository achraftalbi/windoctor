package com.winbit.windoctor.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String DOCTOR = "ROLE_DOCTOR";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String PATIENT = "ROLE_PATIENT";

    public static final String ASSISTANT = "ROLE_ASSISTANT";
}
