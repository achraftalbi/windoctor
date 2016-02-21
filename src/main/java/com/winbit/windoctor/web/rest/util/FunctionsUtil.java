package com.winbit.windoctor.web.rest.util;

import com.winbit.windoctor.common.WinDoctorConstants;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Locale;

/**
 * Created by achraftalbi on 9/28/15.
 */
public class FunctionsUtil {

    private final static Logger log = LoggerFactory.getLogger(FunctionsUtil.class);

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static String convertDateToString(Date date, String format,Locale locale)
    {
        try{
            log.info("date "+date);
            log.info("format "+format);
            String stringDate = locale==null?DateFormatUtils.format(date, format):DateFormatUtils.format(date, format,locale);
            log.info("stringDate "+stringDate);
            return stringDate;
        }catch(Exception ex){
            log.error("Exception in convertDateToString date:"+date+", format:"+format+ " "+ex);
        }
        return null;
    }

    public static String convertDateToString(Date date, String format)
    {
        return convertDateToString(date,format,null);
    }

    public static Date convertStringToDate(String stringDate, String format)
    {
        DateFormat df = new SimpleDateFormat(format);
        try{
            Date date = df.parse(stringDate);
            return date;
        }catch(Exception ex){
            log.error("Exception in convertStringToDate date:"+stringDate+", format:"+format+ " "+ex);
        }
        return null;
    }

    public static Date convertStringToDate(String stringDate, String format, String timezone)
    {
        DateFormat df = new SimpleDateFormat(format);
        if(!isEmpty(timezone)){
            df.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        try{
            Date date = df.parse(stringDate);
            return date;
        }catch(Exception ex){
            log.error("Exception in convertStringToDate date:"+stringDate+", format:"+format+ " "+ex);
        }
        return null;
    }

    public static DateTime convertStringToDateTimeUTC(String stringDate, String format)
    {
        try{
            DateTimeFormatter formatter = org.joda.time.format.DateTimeFormat.forPattern(format);
            DateTime dateTimeInUTC = formatter.withZoneUTC().parseDateTime(stringDate);
            return dateTimeInUTC;
        }catch(Exception ex){
            log.error("Exception in convertStringToDateTimeUTC date:"+stringDate+", format:"+format+ " "+ex);
        }
        return null;
    }

    public static boolean isEmpty(String string){
        if(string==null || string.length()==0){
            return true;
        }
        return false;
    }

    public static boolean stringExist(List<String> list, String string){
        if(list!=null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                System.out.println("string "+string+" list(i) "+list.get(i));
                if(list.get(i).equals(string)){
                    return true;
                }
            }
        }
        return false;
    }

}
