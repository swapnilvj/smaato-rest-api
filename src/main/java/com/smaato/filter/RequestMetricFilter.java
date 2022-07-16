package com.smaato.filter;

import com.smaato.service.RequestMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.util.Date;

import static com.smaato.util.RestServiceConstants.DATE_FORMAT;

@Component
public class RequestMetricFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(RequestMetricFilter.class);

    private RequestMetricService metricService;

    @Autowired
    public RequestMetricFilter(RequestMetricService metricService) {
        this.metricService = metricService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws java.io.IOException, ServletException {
        String time = DATE_FORMAT.format(new Date());
        request.setAttribute("timestamp", time);

        //TODO remove logger before submit
        String id = (String) request.getParameter("id");
        logger.info("id:" + id);
        if(id != null) {
            metricService.increaseRequestCount(time, id);
        }

        chain.doFilter(request, response);

    }
}
