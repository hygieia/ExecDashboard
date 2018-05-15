package com.capitalone.dashboard.exec.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FilterType {
    ITEM("item"),
    SEVERITY("severity");

    private static Map<String, FilterType> fromStringMap = Arrays.stream(values())
            .collect(Collectors.toMap(x -> x.name, Function.identity()));

    public static FilterType fromString(String string) {
        return fromStringMap.get(string);
    }

    private final String name;

    FilterType(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}
