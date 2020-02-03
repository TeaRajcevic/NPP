package com.rajcevic.tea.DiaryWebApp.metrics;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MetricService implements IMetricService {

    private ConcurrentMap<String, ConcurrentHashMap<Integer, Integer>> metricMap;
    private ConcurrentMap<Integer, Integer> statusMetric;
    private ConcurrentMap<String, ConcurrentHashMap<Integer, Integer>> timeMap;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public MetricService() {
        super();
        metricMap = new ConcurrentHashMap<>();
        statusMetric = new ConcurrentHashMap<>();
        timeMap = new ConcurrentHashMap<>();
    }

    // API
    @Override
    public void increaseCount(final String request, final int status) {
        increaseMainMetric(request, status);
        increaseStatusMetric(status);
        updateTimeMap(status);
    }

    @Override
    public Map getFullMetric() {
        return metricMap;
    }

    @Override
    public Map getStatusMetric() {
        return statusMetric;
    }

    // NON-API
    private void increaseMainMetric(final String request, final int status) {
        createMetricMap(request, status, metricMap);
    }

    private void increaseStatusMetric(final int status) {
        final Integer statusCount = statusMetric.get(status);
        if (statusCount == null) {
            statusMetric.put(status, 1);
        } else {
            statusMetric.put(status, statusCount + 1);
        }
    }

    private void updateTimeMap(final int status) {
        final String time = dateFormat.format(new Date());
        createMetricMap(time, status, timeMap);
    }

    private void createMetricMap(String request, int status, ConcurrentMap<String, ConcurrentHashMap<Integer, Integer>> metricMap) {
        ConcurrentHashMap<Integer, Integer> statusMap = metricMap.get(request);
        if (statusMap == null) {
            statusMap = new ConcurrentHashMap<>();
        }

        Integer count = statusMap.get(status);
        if (count == null) {
            count = 1;
        } else {
            count++;
        }
        statusMap.put(status, count);
        metricMap.put(request, statusMap);
    }
}
