package com.winbit.windoctor.common;

/**
 * Created by boufnichel on 15/11/2015.
 */
public class WinDoctorConstants {
    public interface Mail{
        public static final Long PATIENT_CREATION_EMAIL_TYPE = Long.valueOf(1);
        public static final Long DOCTOR_CREATION_EMAIL_TYPE = Long.valueOf(2);
        public static final Long EVENT_CREATION_EMAIL_TYPE = Long.valueOf(3);
        public static final Long EVENT_CANCELATION_EMAIL_TYPE = Long.valueOf(4);
        public static final Long BEFORE_EVENT_START_EMAIL_TYPE = Long.valueOf(5);
    }

    public interface EventStatus{
        public static final Long EVENT_CREATION = Long.valueOf(1);
        public static final Long EVENT_CANCELATION = Long.valueOf(2);

    }

    public interface WinDoctorPattern{
        public static final String EMAIL_DATE_PATTERN = "yyyy.MM.dd '(' HH:mm:ss ')'";
    }
}
