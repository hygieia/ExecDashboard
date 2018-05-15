package com.capitalone.dashboard.exec.collector;

import com.capitalone.dashboard.exec.config.DataFrameLoader;
import com.capitalone.dashboard.exec.model.CollectorType;
import com.capitalone.dashboard.exec.model.HygieiaSparkQuery;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.Iterator;
import scala.collection.mutable.WrappedArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardCollectorItemMapBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashBoardCollectorItemMapBuilder.class);
    private static final String DELIMITER = "|";


    static public class DashboardCollectorItem {
        public String name;
        public String dashboardId;
        protected List<String> items;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDashboardId() {
            return dashboardId;
        }

        public void setDashboardId(String dashboardId) {
            this.dashboardId = dashboardId;
        }

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }

    }

    private static final MapFunction<Row, DashboardCollectorItem> GET_DASHBOARD_COLLECTOR_ITEMS
            = (MapFunction<Row, DashboardCollectorItem>) row -> {
                String productName = row.getAs("productName");
                String componentName = row.getAs("componentName");
                String dashboardId = (String) ((GenericRowWithSchema) row.getAs("dashboardId")).values()[0];
                String dashboardTitle = row.getAs("title");
                String key = productName + DELIMITER + componentName + DELIMITER + dashboardTitle;
                DashboardCollectorItem dashboardCollectorItem = new DashboardCollectorItem();
                dashboardCollectorItem.setName(key);
                dashboardCollectorItem.setDashboardId(dashboardId);
                WrappedArray itemArray = row.getAs("collectorItems");
                Iterator iter = itemArray.iterator();
                List<String> itemIds = new ArrayList<>();
                while (iter.hasNext()) {
                    GenericRowWithSchema grs = (GenericRowWithSchema) iter.next();
                    itemIds.add((String) grs.get(0));
                }
                dashboardCollectorItem.setItems(itemIds);
                return dashboardCollectorItem;
            };


    /**
     * @return Map of (Dashboard Name, CollectorItemId list))
     */
    public static Map<String, List<String>> getDashboardNameCollectorItemsMapById(CollectorType collectorType,
                                                                                    SparkSession sparkSession,
                                                                                    JavaSparkContext javaSparkContext) {
        if ((collectorType == null) || (sparkSession == null) || (javaSparkContext == null)) { return null; }

        LOGGER.info("Start building dashboard collector items map for collectorType =" + collectorType);

        Map<String, List<String>> dashboardCollectorItemsMap = new HashMap<>();
        try {
            DataFrameLoader.loadDataFrame("dashboards", javaSparkContext);
            DataFrameLoader.loadDataFrame("components", javaSparkContext);
            Dataset<Row> dashboardRows = sparkSession.sql(HygieiaSparkQuery.DASHBOARD_QUERY_EXPLODE);
            String query = String.format(HygieiaSparkQuery.COMPONENT_QUERY_BY_COLLECTOR_TYPE, collectorType.toString());
            Dataset<Row> componentRows = sparkSession.sql(query);
            Dataset<Row> dbcompRows = dashboardRows.join(componentRows,
                    componentRows.col("_id").equalTo(dashboardRows.col("widgets.componentId")));
            Dataset<DashboardCollectorItem> dbCollectorItemDataset
                    = dbcompRows.map(GET_DASHBOARD_COLLECTOR_ITEMS, Encoders.bean(DashboardCollectorItem.class));
            List<DashboardCollectorItem> dashboardCollectorItemList = dbCollectorItemDataset.collectAsList();
            dashboardCollectorItemList.forEach(dList -> {
                String collectorItemId = dList.getDashboardId().toString();
                dashboardCollectorItemsMap.put(collectorItemId, dList.getItems());
            });

        } catch (Exception e) {
            LOGGER.error("Exception while building dashboard collectorItems map for collectorType=" + collectorType, e);
        }
        LOGGER.info("End building dashboard collector items map for collectorType =" + collectorType);
        return dashboardCollectorItemsMap;
    }
}