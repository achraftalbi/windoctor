package com.winbit.windoctor.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by achraftalbi on 9/28/15.
 */
public class FunctionsUtil {

    private final Logger log = LoggerFactory.getLogger(FunctionsUtil.class);

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
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
