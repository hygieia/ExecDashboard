package com.capitalone.dashboard.exec.util;

import com.capitalone.dashboard.exec.model.MetricTimeSeriesElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HygieiaExecutiveUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HygieiaExecutiveUtil.class);

    private Double calculateStandardDeviation(List<Double> values) {
        double sum = 0.0;
        double averageValue = getAverageValue(values);

        for(Double value: values) { sum += Math.pow((value - averageValue), 2); }

        return Math.sqrt( sum / ( values.size() - 1 ) );
    }

    private Double getAverageValue(List<Double> values) {
        if (values == null || values.isEmpty()) { return 0.0; }

        Double averageValue = Optional.ofNullable(values)
                                .orElseGet(Collections::emptyList).stream()
                                .mapToDouble(Double::doubleValue)
                                .average()
                                .getAsDouble();
        return averageValue;
    }


    private static int getDaysAgo (String dateStr) throws ParseException{
        int daysAgo = 0;
        Date eventDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        Date currentDate = new Date();
        daysAgo = daysBetween(currentDate, eventDate);
        return daysAgo;
    }

    private static int daysBetween(Date one, Date two) {
        long difference = (one.getTime()-two.getTime())/86400000;
        return (int) Math.abs(difference);
    }

    public static List<MetricTimeSeriesElement> getEmptyTimeSeries() {
        List<MetricTimeSeriesElement> timeSeries = new LinkedList<>();
        for (int count = 0; count < 90; count++) {
            MetricTimeSeriesElement metricTimeSeriesElement = new MetricTimeSeriesElement();
            metricTimeSeriesElement.setDaysAgo(count);
            metricTimeSeriesElement.setCounts(new ArrayList<>());
            timeSeries.add(metricTimeSeriesElement);
        }
        return timeSeries;
    }

    public static int getDaysAgo(Date date) {
        if (date == null) {return -1;}

        String dateString = date.toString();
        try {
            return getDaysAgo(dateString);
        } catch (ParseException e) {
            LOGGER.error("Date parsing error ", e.getStackTrace());
        }
        return 0;
    }
}

