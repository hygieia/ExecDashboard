package com.capitalone.dashboard.exec.config;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.HashMap;
import java.util.Map;

public class DataFrameLoader {

    public static void loadDataFrame(String collectionName, JavaSparkContext javaSparkContext) {
        if (StringUtils.isEmpty(collectionName) || (javaSparkContext == null)) { return; }

        Map<String, String> readOverrides = new HashMap<>();
        readOverrides.put("collection", collectionName);
        ReadConfig readConfig = ReadConfig.create(javaSparkContext).withOptions(readOverrides);
        Dataset<Row> frame = MongoSpark.load(javaSparkContext, readConfig).toDF();
        frame.createOrReplaceTempView(collectionName);
    }
}
