package com.capitalone.dashboard.exec.model;

import com.vividsolutions.jts.util.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class RollupMetricDetailTest {
    private RollupMetricDetail rollupMetricDetail;

    @Before
    public void init() {
        rollupMetricDetail = new RollupMetricDetail();
    }

    @Test
    public void calculateTrendSlopeTest() {
        double values1[] = new double[]{2, 4, 4, 6, 1, 1, 3, 7, 1, 7, 1, 2};
        List<MetricTimeSeriesElement> timeSeriesElements = makeTimeSeriesElements(values1);

        double result = rollupMetricDetail.calculateTrendSlope(timeSeriesElements);
        Assert.equals(result, 1.0);

        double values2[] = new double[]{0, 1, 2, 1, 1, 1, 3, 7, 1, 7, 1, 2};
        timeSeriesElements = makeTimeSeriesElements(values2);

        result = rollupMetricDetail.calculateTrendSlope(timeSeriesElements);
        Assert.equals(result, -1.0);
    }

    private List<MetricTimeSeriesElement> makeTimeSeriesElements(double values[]) {
        List<MetricTimeSeriesElement> timeSeriesElements = new ArrayList<>();

        timeSeriesElements.add(makeTimeSeriesElement(1, values[0]));
        timeSeriesElements.add(makeTimeSeriesElement(10, values[1]));

        timeSeriesElements.add(makeTimeSeriesElement(16, values[2]));
        timeSeriesElements.add(makeTimeSeriesElement(25, values[3]));

        timeSeriesElements.add(makeTimeSeriesElement(31, values[4]));
        timeSeriesElements.add(makeTimeSeriesElement(40, values[5]));

        timeSeriesElements.add(makeTimeSeriesElement(46, values[6]));
        timeSeriesElements.add(makeTimeSeriesElement(55, values[7]));

        timeSeriesElements.add(makeTimeSeriesElement(66, values[8]));
        timeSeriesElements.add(makeTimeSeriesElement(70, values[9]));

        timeSeriesElements.add(makeTimeSeriesElement(76, values[10]));
        timeSeriesElements.add(makeTimeSeriesElement(90, values[11]));

        return timeSeriesElements;
    }

    private MetricTimeSeriesElement makeTimeSeriesElement(int daysAgo, double value) {
        MetricTimeSeriesElement timeSeriesElement = new MetricTimeSeriesElement();
        timeSeriesElement.setDaysAgo(daysAgo);

        List<MetricCount> metricCountsList = new ArrayList<>();
        timeSeriesElement.setCounts(metricCountsList);

        MetricCount metricCount = new MetricCount();
        metricCount.setValue(value);

        Map<String, String> label = new HashMap<>();
        label.put("type", "value");
        metricCount.setLabel(label);
        metricCountsList.add(metricCount);

        return timeSeriesElement;
    }
}
