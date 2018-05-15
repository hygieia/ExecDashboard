package com.capitalone.dashboard.exec.model;

public class Filter {
    private FilterType filterType;
    private String filterValue;

    public Filter (FilterType filterType, String filterValue) {
        this.filterType = filterType;
        this.filterValue = filterValue;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }
}
