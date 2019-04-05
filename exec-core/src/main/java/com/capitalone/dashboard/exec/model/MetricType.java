package com.capitalone.dashboard.exec.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.capitalone.dashboard.exec.model.MetricType.SummaryRange.DAY;
import static com.capitalone.dashboard.exec.model.MetricType.SummaryRange.MULTI_DAY;

public enum MetricType {
    OPEN_SOURCE_VIOLATIONS("open-source", DataType.SUM, DAY, null),
    SECURITY_VIOLATIONS("security-violations", DataType.SUM, DAY, null),
    TEST_AUTOMATION("test-automation", DataType.RATIO, DAY, null),
    STATIC_CODE_ANALYSIS("static-code-analysis", DataType.SUM, DAY, null),
    PRODUCTION_INCIDENTS("production-incidents", DataType.SUM, MULTI_DAY, "mttr"),
    UNIT_TEST_COVERAGE("unit-test-coverage", DataType.RATIO, DAY, null),
    PRODUCTION_RELEASES("production-releases", DataType.SUM, MULTI_DAY, null),
    PIPELINE_LEAD_TIME("pipeline-lead-time", DataType.AVERAGE, DAY, null),
    SCM_COMMITS("scm-commits", DataType.SUM, MULTI_DAY, null),
	QUALITY("quality", DataType.SUM, MULTI_DAY, null),
    VELOCITY("open-source-violations", DataType.AVERAGE, MULTI_DAY, null),
	WORK_IN_PROGRESS("work-in-progress", DataType.SUM, MULTI_DAY, null),
	STASH("stash", DataType.SUM, MULTI_DAY, null),
	TOTAL_VALUE("total-value", DataType.SUM, MULTI_DAY, null),
	DEVOPSCUP("devopscup", DataType.SUM, MULTI_DAY, null),
	SAY_DO_RATIO("saydoratio", DataType.RATIO, MULTI_DAY, null),
	TEST("test", DataType.SUM, MULTI_DAY, null),
	CLOUD("cloud", DataType.SUM, MULTI_DAY, null),
	DEPLOY("deploy", DataType.SUM, MULTI_DAY, null),
	BUILD("build", DataType.SUM, MULTI_DAY, null);

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
