package com.winbit.windoctor.web.rest.util;

import com.winbit.windoctor.web.rest.dto.ErrorDTO;

/**
 * Class to manage exceptions codes
 *
 * @author MBoufnichel
 */
public class ErrorCodes {
    /**
     * Put here all user exception codes
     */
    public interface User{
        public static final ErrorDTO EMAIL_ALREADY_USED = new ErrorDTO("U-01");
        public static final ErrorDTO LOGIN_ALREADY_USED = new ErrorDTO("U-02");
    }
}
