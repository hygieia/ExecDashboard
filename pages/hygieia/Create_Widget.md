---
title: Guidelines to Build a Widget
tags:
type:
toc: true
keywords:
summary:
sidebar: hygieia_sidebar
permalink: Create_Widget.html
---

We welcome your contributions to **The Hygieia Executive Dashboard**. This section explains an overview to create a new Hygieia Executive Dashboard widget end-to-end.

## Guidelines to Create a Collector

Copy the sample collector project into a new directory in the Collectors folder and give it a name to match the collector you are building. Model your collector based on any of the existing collectors such as Code Commit Collector, Static Code Analysis Collector, etc.

Override the following methods:

- ```getMetricType()``` 
  
  Return your specific metric type

- ```getQuery()``` 
  
   - Develop your specific query to fetch data to generate metrics
   - Add it to the class “HygieiaSparkQuery”
   - Return the query in this method.

- ```getCollection()```
  
  Return the name of the collection that your query goes against to fetch data to generate metrics

- ```getCollectorType()```
  
  Return the collector type based on your widget.
	
  The collector type is used to fetch components of that type, to fetch the appropriate collector items.

- ```updateCollectorItemMetricDetail()``` and ```getMetricCount()```
  
  Implement this method to incorporate your custom logic to generate metrics for your widget type.

### Build and Deploy

Run ```mvn clean install``` to package the collector into an executable JAR file. Copy this file to your server and launch it using ```java -JAR [collector-name]-collector.jar```. You will need to provide an ```application.properties``` file that contains information about how to connect to the Dashboard MongoDB database instance, as well as any custom properties that your collector requires. See the Spring Boot [documentation](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-application-property-files) for information about sourcing this properties file.

	
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
portfolio.incidentsCollectorFlag=false
portfolio.scmCollectorFlag=true
```	

## Guidelines for UI

Model your widgets after one of the existing widgets, like Code Commits or Static Code Analysis. Customize your module, component, service, html, strategy and the “.scss” files as needed.



