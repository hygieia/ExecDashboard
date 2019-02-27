---
title: Collectors
tags:
keywords:
summary:
sidebar: hygieia_sidebar
permalink: EXECCollectors_Setup.html
---

## About the Collectors
 
Configure the Hygieia Executive Collectors to display metrics on the Hygieia Executive Dashboard. These metrics give the DevOps state of maturity and risk that are tracked by Hygieia.

Each widget has a collector that generates these metrics and saves the information in MongoDB. The dashboard displays information based on the information saved in MongoDB.

### Portfolio Collector

The Portfolio collector builds the structure of each portfolio. Each portfolio consists of the relation between the executive that owns the portfolio, the products under the portfolio, and the components under the product.

The portfolio collector does the following:

- Passes the list of portfolios to each metrics collector
- Runs the metrics' collectors one after the other

### Metrics Collectors

Each metric collector queries to fetch data from the database, and aggregates the data to generate the count or metrics.

While aggregating the data, the metrics collector uses the list of portfolios passed by the portfolio collector to associate the metrics with a portfolio, product and component or environment combination.

Based on the metric type, each of the corresponding metric collectors then store the aggregated metrics data into its own metric collection in the database.

Hygieia uses Spring Boot to package the collectors as an executable JAR file with dependencies.

## Setup Instructions

To configure the Portfolio and Metric collectors, execute the following steps:

*   **Step 1: Change Directory**

Change the current working directory to the `collectors` directory of your Hygieia Executive source code installation.

In the terminal, run the following command:

```bash
cd C:/Users/[username]/hygieia/analysis/
```

*   **Step 2: Run Maven Build**

Run the maven build to package the collectors into an executable jar file:

```bash
mvn clean install
```

The output file `analysis.jar` is generated in the `analysis/target` folder.

*   **Step 3: Set Parameters in the Application Properties File**

Set the configurable parameters in the `application.properties` file to connect to the analyticsdb MongoDB database instance, including properties required by the Portfolio and Metrics Collectors.

For information about sourcing the application properties file, refer to the [Spring Boot Documentation](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-application-property-files).

To configure parameters for the Portfolio and Metrics Collectors, refer to the sample [application.properties](#sample-application-properties-file) file.

*   **Step 4: Deploy the Executable File**

To deploy the `analysis.jar` file, change directory to `analysis/target`, and then execute the following from the command prompt:

```bash
java -jar ./analysis.jar -Djasypt.encryptor.password=hygieiasecret --spring.config.location=[path to application.properties file]
```

### Sample Application Properties File

The sample `application.properties` file lists parameter values to configure the Hygieia Executive Collectors. Set the parameters based on your environment setup.

```properties
# MongoDB Details
dbname=<db_name>
dbusername=<db_user>
dbpassword=<db_pwd>
dbhostport=<hostname>:<port>

logging.file=<log file path>

portfolio.cron=0 0/30 * * * *
portfolio.readUriUserName=
portfolio.readUriPassword=
portfolio.readUriDatabase=<hostname>:<port>
portfolio.readUriPrefix=mongodb
portfolio.readDatabase=<db_name>
portfolio.writeUri=mongodb://<hostname>:<port>
portfolio.writeDatabase=<db_name>
portfolio.filters=scm-commits:item:environment|production-incidents:severity:4.0|

#Enable/Disable SCM Collector
portfolio.scmCollectorFlag=false

#Enable/Disable Incident Collector
portfolio.incidentsCollectorFlag=false

#Enable/Disable Library Policy Collector
portfolio.libraryPolicyCollectorFlag=false

#Enable/Disable Static Code Collector
portfolio.staticCodeAnalysisCollectorFlag=false

#Enable/Disable Unit Test Collector
portfolio.unitTestCoverageCollectorFlag=false

#Enable/Disable AuditResult Collector
portfolio.auditResultCollectorFlag=false

#Enable/Disable Security Collector
portfolio.securityCollectorFlag=false
```	
