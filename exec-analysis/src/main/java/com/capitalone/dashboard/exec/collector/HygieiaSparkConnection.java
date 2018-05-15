package com.capitalone.dashboard.exec.collector;

import org.apache.spark.sql.SparkSession;

public class HygieiaSparkConnection {
    private final String readUri;
    private final String writeUri;
    private final String readDatabase;
    private final String writeDatabase;

    public HygieiaSparkConnection(String readUri, String readDatabase, String writeUri, String writeDatabase) {
        this.readUri = readUri;
        this.readDatabase = readDatabase;
        this.writeUri = writeUri;
        this.writeDatabase = writeDatabase;
    }

    public SparkSession getInstance() {
        return SparkSession.builder()
                .master("local")
                .appName("HygieiaPortfolioCollector")
                .config("spark.mongodb.input.uri", this.readUri)
                .config("spark.mongodb.input.database", readDatabase)
                .config("spark.mongodb.input.collection", "dummy")
                .config("spark.mongodb.output.uri", writeUri)
                .config("spark.mongodb.output.database", writeDatabase)
                .config("spark.mongodb.output.collection", "dummy")
                .getOrCreate();
    }
}