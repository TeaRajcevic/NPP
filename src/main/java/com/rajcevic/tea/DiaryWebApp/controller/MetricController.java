package com.rajcevic.tea.DiaryWebApp.controller;

import com.rajcevic.tea.DiaryWebApp.metrics.IMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class MetricController {

    @Autowired
    private IMetricService metricService;

    public MetricController() {
        super();
    }

    @RequestMapping(value = "/metric", method = RequestMethod.GET)
    @ResponseBody
    public Map getMetric() {
        return metricService.getFullMetric();
    }

    @RequestMapping(value = "/status-metric", method = RequestMethod.GET)
    @ResponseBody
    public Map getStatusMetric() {
        return metricService.getStatusMetric();
    }
}
