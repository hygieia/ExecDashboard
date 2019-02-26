package com.capitalone.dashboard.exec.model;

public class HygieiaSparkQuery {
    public static final String CMDB_PRODUCT_QUERY =
            "SELECT _id as productId, configurationItem as productName, commonName, environments, components, businessOwner, ownerDept, appServiceOwner, supportOwner, developmentOwner " +
                    "FROM cmdb where (validConfigItem = 1) and (businessOwner is not null) and (businessOwner != '') and (itemType = 'app')";

    public static final String CMDB_COMPONENT_QUERY =
            "SELECT _id as componentId, configurationItem, commonName as componentName, businessOwner, ownerDept " +
                    "FROM cmdb where (validConfigItem = 1) and (itemType = 'component')";

    public static final String CMDB_ENVIRONMENT_QUERY =
            "SELECT _id as componentId, configurationItem, components, commonName as componentName, businessOwner, ownerDept " +
                    "FROM cmdb where (itemType = 'environment')";

    public static final String SCM_COMMITS_QUERY =
            " SELECT collectorItemId, cast(from_unixtime(cast(scmCommitTimestamp/1000 as bigint)) as timestamp) as timeWindow, " +
                    " cast(count(*) as double) as count " +
                    " FROM commits " +
                    " GROUP BY collectorItemId, cast(from_unixtime(cast(scmCommitTimestamp/1000 as bigint)) as timestamp) " +
                    " ORDER BY timeWindow ASC";

    public static final String OPEN_SOURCE_QUERY_ALL_COLLECTOR_ITEMS =
            " SELECT collectorItemId, threats.Security as security, threats.License as license, cast(from_unixtime(cast(timestamp/1000 as bigint)) as timestamp) as timeWindow " +
                    " FROM library_policy " +
                    "WHERE (threats.Security IS NOT NULL or threats.License IS NOT NULL) " +
                    " ORDER BY timeWindow ASC";

    public static final String DASHBOARD_QUERY_EXPLODE =
            "SELECT _id as dashboardId, title, explode(widgets) as widgets, widgets.componentId as componentId, configurationItemBusServName as productName, configurationItemBusAppName as componentName " +
                    "FROM dashboards WHERE (configurationItemBusServName IS NOT null) and (configurationItemBusAppName IS NOT null) and (type = 'Team')";

    public static final String COMPONENT_QUERY_BY_COLLECTOR_TYPE =
            "SELECT _id, collectorItems.%1$s._id as collectorItems FROM components where collectorItems.%1$s IS NOT NULL";

    public static final String STATIC_CODE_ANALYSIS_QUERY_ALL_COLLECTOR_ITEMS =
            "SELECT collectorItemId, metrics, cast(from_unixtime(cast(timestamp/1000 as bigint)) as timestamp) as timeWindow " +
                    "FROM code_quality " +
                    "WHERE (metrics IS NOT NULL) " +
                    "ORDER BY timeWindow ASC";
    public static final String SECURITY_ANALYSIS_QUERY_ALL_COLLECTOR_ITEMS =
            "SELECT collectorItemId, metrics, type, cast(from_unixtime(cast(timestamp/1000 as bigint)) as timestamp) as timeWindow " +
                    "FROM code_quality " +
                    "WHERE (metrics IS NOT NULL) and (type = 'SecurityAnalysis') " +
                    "ORDER BY timeWindow ASC";

    public static final String TRACEABILITY_QUERY_ALL_COLLECTOR_ITEMS =
            "SELECT collectorItemId, traceability, cast(from_unixtime(cast(timestamp/1000 as bigint)) as timestamp) as timeWindow " +
                    "FROM audit_results " +
                    "WHERE (traceability IS NOT NULL) " +
                    "ORDER BY timeWindow ASC";

    public static final String CMDB_INCIDENT_QUERY =
            "SELECT collectorItemId, severity, status, cast(from_unixtime(cast(openTime/1000 as bigint)) as timestamp) as timeWindow, " +
                    "cast(from_unixtime(cast(closedTime/1000 as bigint)) as timestamp) as closeDate, closedTime, " +
                    "cast(count(*) as double) as count " +
                    "FROM incident " +
                    "WHERE LTRIM(RTRIM(IFNULL(affectedItem, ''))) != '' and severity in ('1','2','3','3C','3D') " +
                    " and LTRIM(RTRIM(IFNULL(status, ''))) != '' and UCASE(status) in ('OPEN', 'OPENED', 'CLOSE', 'CLOSED')" +
                    "GROUP BY collectorItemId, severity, status, cast(from_unixtime(cast(openTime/1000 as bigint)) as timestamp), " +
                    "cast(from_unixtime(cast(closedTime/1000 as bigint)) as timestamp), closedTime " +
                    "ORDER BY collectorItemId, timeWindow, closeDate";
}
