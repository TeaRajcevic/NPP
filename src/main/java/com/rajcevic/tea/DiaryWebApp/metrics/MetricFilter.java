package com.rajcevic.tea.DiaryWebApp.metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MetricFilter implements Filter {

    @Autowired
    private IMetricService metricService;

    @Override
    public void init(final FilterConfig config) throws ServletException {
        if (metricService == null) {
            metricService = (IMetricService) WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext()).getBean("metricService");
        }
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws java.io.IOException, ServletException {
        final HttpServletRequest httpRequest = ((HttpServletRequest) request);
        final String req = httpRequest.getMethod() + " " + httpRequest.getRequestURI();

        chain.doFilter(request, response);

        final int status = ((HttpServletResponse) response).getStatus();
        metricService.increaseCount(req, status);
    }

    @Override
    public void destroy() {

    }
}
