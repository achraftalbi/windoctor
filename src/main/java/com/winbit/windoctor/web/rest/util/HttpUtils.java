package com.winbit.windoctor.web.rest.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by boufnichel on 15/11/2015.
 */
public class HttpUtils {

    public static String getBaseUrl(HttpServletRequest request){
        String baseUrl = request.getScheme() + // "http"
            "://" +                                // "://"
            request.getServerName() +              // "myhost"
            ":" +                                  // ":"
            request.getServerPort();               // "80"

        return  baseUrl;
    }
}
