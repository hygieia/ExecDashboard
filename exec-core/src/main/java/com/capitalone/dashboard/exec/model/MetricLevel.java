package com.capitalone.dashboard.exec.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MetricLevel {
    LOB("lob"),
    PORTFOLIO("portfolio"),
    PRODUCT("product"),
    ENVIRONMENT("environment"),
    COMPONENT("component"),
    COLLECTOR_ITEM ("collector_item");

    private static Map<String, MetricLevel> fromStringMap = Arrays.stream(values())
            .collect(Collectors.toMap(x -> x.level, Function.identity()));

    public static MetricLevel fromString(String string) {
        return fromStringMap.get(string);
    }

    private final String level;

    MetricLevel(String level) {
        this.level = level;
    }

    public String getLevel() { return level; }
}
