package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.config.DataFrameLoader;
import com.capitalone.dashboard.exec.util.HygieiaExecutiveUtil;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class DefaultDataCollector {
    private List<String> collectorItemIds;
    private SparkSession sparkSession;
    private String collectionName;
    private String query;
    private JavaSparkContext javaSparkContext;

    DefaultDataCollector(String collectionName, String query, List<String> collectorItemIds, SparkSession sparkSession, JavaSparkContext javaSparkContext) {
        this.collectionName = collectionName;
        this.query = query;
        this.collectorItemIds = collectorItemIds;
        this.sparkSession = sparkSession;
        this.javaSparkContext = javaSparkContext;
    }

    public Map<String, List<Row>> collectAll() {
        Map<String, List<Row>> rowMap = new HashMap<>();
        DataFrameLoader.loadDataFrame(collectionName, javaSparkContext);
        Dataset<Row> dataRows = sparkSession.sql(query);
        List<Row> rowList = dataRows.collectAsList();
            rowList.forEach(row -> {
                String item = (String) ((GenericRowWithSchema) row.getAs("collectorItemId")).get(0);
                boolean matchingCollectorItem = !Objects.equals(
                        Optional.ofNullable(collectorItemIds).orElseGet(Collections::emptyList).stream()
                        .filter(Predicate.isEqual(item)).findFirst().orElse(""), "");
                Date timeWindowDt = row.getAs("timeWindow");
                long daysAgo = HygieiaExecutiveUtil.getDaysAgo(timeWindowDt);
                if (matchingCollectorItem && (daysAgo < 90)) {
                    String collectorItemId = (String) ((GenericRowWithSchema) row.getAs("collectorItemId")).get(0);
                    List<Row> existingRowList = rowMap.get(collectorItemId);
                    if (existingRowList == null) {
                        List<Row> newRow = new ArrayList<>();
                        newRow.add(row);
                        rowMap.put(collectorItemId, newRow);
                    } else {
                        existingRowList.add(row);
                        rowMap.put(collectorItemId, existingRowList);
                    }
                }
            });
        return rowMap;
    }

    public Map<String, List<Row>> collectAll(boolean isDashboarConfigNeeded) {
        Map<String, List<Row>> rowMap = new HashMap<>();
        DataFrameLoader.loadDataFrame(collectionName, javaSparkContext);
        Dataset<Row> dataRows = sparkSession.sql(query);
        List<Row> rowList = dataRows.collectAsList();
        rowList.forEach(row -> {
            Date timeWindowDt = row.getAs("timeWindow");
            long daysAgo = HygieiaExecutiveUtil.getDaysAgo(timeWindowDt);
            if ((daysAgo < 90)) {
                String dashboardId = (String) ((GenericRowWithSchema) row.getAs("dashboardId")).get(0);
                List<Row> existingRowList = rowMap.get(dashboardId);
                if (existingRowList == null) {
                    List<Row> newRow = new ArrayList<>();
                    newRow.add(row);
                    rowMap.put(dashboardId, newRow);
                } else {
                    existingRowList.add(row);
                    rowMap.put(dashboardId, existingRowList);
                }
            }
        });
        return rowMap;
    }
}