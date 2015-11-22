package com.winbit.windoctor.service.util;

import com.winbit.windoctor.common.WinDoctorConstants;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Boufnichel on 22/11/2015.
 */
public class DateUtil {

    public static String formatDate(DateTime date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WinDoctorConstants.WinDoctorPattern.EMAIL_DATE_PATTERN);
        return simpleDateFormat.format(date.toDate());
    }

    public static String getFormattedTime(DateTime date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WinDoctorConstants.WinDoctorPattern.EMAIL_SHORT_DATE_PATTERN);
        return simpleDateFormat.format(date.toDate());
    }
}
