package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.config.DataFrameLoader;
import com.capitalone.dashboard.exec.util.HygieiaExecutiveUtil;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataCollector.class);
    private List<String> collectorItemIds;
    private SparkSession sparkSession;
    private String collectionName;
    private String query;
    private JavaSparkContext javaSparkContext;
    private PortfolioCollectorSetting portfolioCollectorSetting;

    DefaultDataCollector(String collectionName, String query, List<String> collectorItemIds, SparkSession sparkSession, JavaSparkContext javaSparkContext, PortfolioCollectorSetting portfolioCollectorSetting) {
        this.collectionName = collectionName;
        this.query = query;
        this.collectorItemIds = collectorItemIds;
        this.sparkSession = sparkSession;
        this.javaSparkContext = javaSparkContext;
        this.portfolioCollectorSetting = portfolioCollectorSetting;
    }

    public Map<String, List<Row>> collectAll() {
        Map<String, List<Row>> rowMap = new HashMap<>();
        DataFrameLoader.loadDataFrame(collectionName, javaSparkContext);
        Dataset<Row> dataRows = null;
        if (collectionName.contains("pipelines")) {
            dataRows = getDataRowsForPipelines();
        }
        else {
            dataRows = sparkSession.sql(query);
        }
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

    protected Dataset<Row> getDataRowsForPipelines() {
        Dataset<Row> dataRows = null;
        String tempQuery = query;
        List<String> envName = portfolioCollectorSetting.getPipelineEnvName();
        try {
            for (String eachEnvName : envName) {
                Dataset<Row> partialDataRows = null;
                query = String.format(tempQuery, eachEnvName);
                if (dataRows == null) {
                    dataRows = sparkSession.sql(query);
                } else {
                    partialDataRows = sparkSession.sql(query);
                    dataRows = dataRows.union(partialDataRows);
                }
                LOGGER.info("Final DataRows for Pipeline:" + dataRows.toString());
            }
        }catch(Exception analysisException){
            LOGGER.info("Analysis Exception thrown for struct field:" + envName);
        }
        return dataRows;
    }
}