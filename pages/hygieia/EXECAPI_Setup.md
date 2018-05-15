---
title: The Hygieia Executive Dashboard API
tags:
keywords:
summary:
sidebar: hygieia_sidebar
permalink: EXECAPI_Setup.html
---

**The Hygieia Executive Dashboard** API layer contains all common REST API services that work with the source system data, which the service tasks collect.

Hygieia uses Spring Boot to package the API as an executable JAR file with dependencies.

## Setup Instructions

To configure the Hygieia Executive API layer, execute the following steps:

*	**Step 1: Run Maven Build**

	To package the API source code into an executable JAR file, run the Maven build from the `/HygieiaExecutive` directory of your source code installation:

	```bash
	mvn clean install
	```

	The output file `api.jar` is generated in the `/api/target` folder.

*	**Step 2: Set Parameters in the API Properties File**

	Set the configurable parameters in the `api.properties` file to connect to the dashboard MongoDB database instance, including properties required by the API module. To configure the parameters, refer to the [API properties](#api-properties-file) file.

	For more information about the server configuration, see the Spring Boot [documentation](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-application-property-files).

*	**Step 3: Run the API**

	To run the executable file, change directory to 'api/target' and then execute the following command from the command prompt:

	```bash
	java -jar ./api.jar -Djasypt.encryptor.password=hygieiasecret --spring.config.location=[path to]/Hygieia/api/api.properties -Djasypt.encryptor.password=hygieiasecret 
	
	#[-Djasypt.encryptor.password= This is required for passing encrypted passwords]
	```

	Verify API access from the web browser using the URL: http://localhost:8080/api/ping.

	By default, the server starts at port `8080` and uses the context path `/api`. You can configure these values in the `api.properties` file for the following properties:

	```properties
	server.contextPath=/api
	server.port=8080
	```
	**Note**: The 'jasypt.encryptor.password' system property is used to decrypt the database password. 
	
## API Properties File

The sample `api.properties` file lists parameters with sample values to configure the API layer. Set the parameters based on your environment setup.

```properties
dbname=<b_name>
dbusername=<db_user>
dbpassword=<db_pwd>
dbhostport=<hostname>:<port>

# IP of the Tomcat server running the API	
server.address=<host_ip>(default is 8080)
server.port=<server port running the api>
```

