package com.capitalone.dashboard.exec.converters;

import com.capitalone.dashboard.exec.model.Filter;
import com.capitalone.dashboard.exec.model.FilterType;
import com.capitalone.dashboard.exec.model.MetricType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@ConfigurationPropertiesBinding
public class FilterConverter implements Converter<String, Map<MetricType, List<Filter>>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterConverter.class);
    private static final String INTER_FILTER_DELIMITER = "|";
    private static final String INTRA_FILTER_DELIMITER = ":";

    @Override
    public Map<MetricType, List<Filter>> convert(String source) {
        if((source == null) || source.isEmpty()) {
            LOGGER.info("No filter information found, returning ...");
            return null;
        }

        String[] filters = source.split(Pattern.quote(INTER_FILTER_DELIMITER));
        if ((filters == null) || (filters.length == 0)) {
            LOGGER.info("Filter information is not set appropriately, returning ...");
            return null;
        }

        Map<MetricType, List<Filter>> filterMap = new HashMap<>();
        List<String> filterList = Arrays.asList(filters);
        Optional.ofNullable(filterList)
                .orElse(new ArrayList<>()).stream()
                .forEach(f -> {
                    String[] filterParts = f.split(Pattern.quote(INTRA_FILTER_DELIMITER));
                    if ((filterParts != null) && (filterParts.length == 3)) {
                        addFilter(filterParts, filterMap);
                    }
                });

        return filterMap;
    }

    private void addFilter(String[] filterParts, Map<MetricType, List<Filter>> filterMap) {
        MetricType metricType = MetricType.fromString(filterParts[0]);
        FilterType filterType = FilterType.fromString(filterParts[1]);
        String filterValue = filterParts[2];

        // This returns an empty list when there is no match, not null.
        List<Filter> filterList = filterMap.entrySet().stream()
                                    .filter(e -> e.getKey().equals(metricType))
                                    .flatMap(e -> Optional.ofNullable(e.getValue())
                                                    .orElseGet(Collections::emptyList).stream())
                                    .collect(Collectors.toList());
        if (filterList.isEmpty()) {
            filterMap.put(metricType, filterList);
        }
        Filter filter = new Filter(filterType, filterValue);
        filterList.add(filter);
    }
}
