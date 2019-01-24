package com.capitalone.dashboard.exec.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.capitalone.dashboard.exec.model.MetricType.SummaryRange.DAY;
import static com.capitalone.dashboard.exec.model.MetricType.SummaryRange.MULTI_DAY;

public enum MetricType {
    OPEN_SOURCE_VIOLATIONS("open-source-violations", DataType.SUM, DAY, null),
    SECURITY_VIOLATIONS("security-violations", DataType.SUM, DAY, null),
    TEST_AUTOMATION("test-automation", DataType.RATIO, DAY, null),
    STATIC_CODE_ANALYSIS("static-code-analysis", DataType.SUM, DAY, null),
    PRODUCTION_INCIDENTS("production-incidents", DataType.SUM, MULTI_DAY, "mttr"),
    UNIT_TEST_COVERAGE("unit-test-coverage", DataType.RATIO, DAY, null),
    PRODUCTION_RELEASES("production-releases", DataType.SUM, MULTI_DAY, null),
    PIPELINE_LEAD_TIME("pipeline-lead-time", DataType.AVERAGE, DAY, null),
    SCM_COMMITS("scm-commits", DataType.SUM, MULTI_DAY, null),
    TRACEABILITY("Traceability", DataType.RATIO, DAY, null);

    public enum DataType { SUM, RATIO, AVERAGE }
    public enum SummaryRange { DAY, MULTI_DAY }

    private static Map<String, MetricType> fromStringMap = Arrays.stream(values())
            .collect(Collectors.toMap(x -> x.name, Function.identity()));

    public static MetricType fromString(String string) {
        return fromStringMap.get(string);
    }

    private final String name;
    private final DataType dataType;
    private final SummaryRange summaryRange;
    private final String secondaryTimeLabel;

    MetricType(String name, DataType dataType, SummaryRange summaryRange, String secondaryTimeLabel) {
        this.name = name;
        this.dataType = dataType;
        this.summaryRange = summaryRange;
        this.secondaryTimeLabel = secondaryTimeLabel;
    }

    public String getName() { return name; }
    public DataType getDataType() { return dataType; }
    public SummaryRange getSummaryRange() { return summaryRange; }
    public String getSecondaryTimeLabel() { return secondaryTimeLabel; }
}
