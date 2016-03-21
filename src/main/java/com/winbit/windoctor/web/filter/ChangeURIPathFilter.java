package com.winbit.windoctor.web.filter;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter is used to change the path of the URL, to the current structure URL.
 * <p/>
 * <p>
 * It is configured to serve resources from the "dist" directory, which is the Grunt
 * destination directory.
 * </p>
 */
public class ChangeURIPathFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(ChangeURIPathFilter.class);

    RequestWrapper modifiedRequest = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to initialize
    }

    @Override
    public void destroy() {
        // Nothing to destroy
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String contextPath = ((HttpServletRequest) request).getContextPath();
        String requestURI = httpRequest.getRequestURI();
        //requestURI = StringUtils.substringAfter(requestURI, contextPath);
        /*log.info(" requestURI AAA --> "+requestURI);
        System.out.println("called here the filter");
        if (StringUtils.equals("/", requestURI)) {
            requestURI = "/index.html";
        }
        //String newURI = "/dist" + requestURI;
        //HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //httpServletResponse.sendRedirect("/");
        System.out.println("redirection down 2");*/
        //request.getRequestDispatcher("/").forward(request, response);
        /*if (StringUtils.equals("/", requestURI)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect("/");
        }else{
            chain.doFilter(modifiedRequest, response);
        }*/
        /*if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {

            if (requestURI.indexOf("/portal") >= 0) {
                modifiedRequest = new RequestWrapper(httpRequest);
                modifiedRequest.changeDestinationAgent("/portal", "/");
            }

            log.info(" requestURI --> Allo");

            chain.doFilter(modifiedRequest, response);
        }else{
            chain.doFilter(request, response);
        }*/
        /*log.info("modify display URI requestURI " + requestURI);
        //String newURI = requestURI;
        //modifiedRequest = new RequestWrapper(httpRequest,"/");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (requestURI.indexOf("test") >= 0 && httpServletRequest.getSession().getAttribute("STRUCTURE_SET_IN_SESSION")==null) {
            httpServletRequest.getSession().setAttribute("STRUCTURE_SET_IN_SESSION",true);
            request.getRequestDispatcher("/").forward(request, response);
            log.info("first");
        }else if (requestURI.equals("/") &&
            httpServletRequest.getSession().getAttribute("STRUCTURE_SET_IN_SESSION")==null) {
            request.getRequestDispatcher("/404.html").forward(request, response);
            log.info("third");
        }else{
            httpServletRequest.getSession().setAttribute("STRUCTURE_SET_IN_SESSION",null);
            chain.doFilter(request, response);
            log.info("fourth");
        }*/
        chain.doFilter(request, response);
    }


    /**
     * A class to wrap an <code>HttpServletRequest</code> so we can modify parts of the request
     */
    class RequestWrapper extends HttpServletRequestWrapper {

        private String originalValue;
        private String newValue;

        /*
         * Constructor
         */
        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }

        /*
         *
         * (non-Javadoc)
         * @see javax.servlet.http.HttpServletRequestWrapper#getRequestURI()
         */
        @Override
        public String getRequestURI() {
            String originalURI = super.getRequestURI();

            /*StringBuffer newURI = new StringBuffer();

            newURI.append(originalURI.substring(0, originalURI.indexOf(originalDestination)));
            newURI.append(newDestinationAgent);
            newURI.append(originalURI.substring(originalURI.indexOf(originalDestination) + originalDestination.length(),
                originalURI.length()));

            return newURI.toString();*/
            log.info("originalURI old "+originalURI);
            //originalURI = originalURI==null?originalURI:originalURI.replaceAll("test","");
            log.info("originalURI new " + originalURI);
            //return originalURI.toString();
            String s = super.getRequestURI();
            if (StringUtils.equals(s, originalValue))
                return originalValue.replaceAll("test","");
            else
                return s;
        }

        public RequestWrapper(HttpServletRequest request, String newValue) {
            super(request);
            this.newValue = newValue;
            this.originalValue = request.getRequestURI();
        }

        /**
         * Change the original destination agent/queue manager set in the request by the
         * HTTP client (or a previous filter) to a new destination agent/queue manager.
         *
         * @param originalDestination
         * @param newDestination
         */
        /*protected void changeDestinationAgent(String originalDestination, String newDestination) {
            this.originalDestination = originalDestination;
            this.newDestinationAgent = newDestination;
        }*/

    }
}
