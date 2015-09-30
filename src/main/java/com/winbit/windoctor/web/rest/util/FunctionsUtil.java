package com.winbit.windoctor.web.rest.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by achraftalbi on 9/28/15.
 */
public class FunctionsUtil {


    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

}
