package com.rajcevic.tea.DiaryWebApp.metrics;

import java.util.Map;

public interface IMetricService {
    void increaseCount(final String request, final int status);
    Map getFullMetric();
    Map getStatusMetric();
}
