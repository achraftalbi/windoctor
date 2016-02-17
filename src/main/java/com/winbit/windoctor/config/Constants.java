package com.winbit.windoctor.config;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String CURRENT_STRUCTURE = "current_structure";

    public static final String CURRENT_USER_LOGIN = "current_user_login";

    //Event constants
    public static final Long STATUS_BLOCKED = 9l;
    public static final Long STATUS_IN_PROGRESS = 1l;
    public static final Long STATUS_VISIT = 8l;
    public static final Long STATUS_ANNULED = 2l;
    public static final Long STATUS_ANNULED_BY_PATIENT = 10l;
    public static final Long STATUS_REQUEST = 7l;


    //WinDoctor application format
    public static final String GLOBAL_DATE_FORMAT = "yy/MM/yyyy";
    public static final String GLOBAL_HOUR_MINUTE = "HH:mm";


    // Special characters
    public static final String PERCENTAGE = "%";

    private Constants() {
    }
}
