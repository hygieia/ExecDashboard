# API Configuration

Create a file in this folder named application.properties and enter the following properties:

```
server.contextPath=/api
server.port=8080

dbhost=exec-db
dbport=27017

dbname=analyticsdb
dbusername=analyticsuser
dbpassword=analyticspass

dbreplicaset=false

logRequest=false
logSplunkRequest=false
corsEnabled=false

version.number=@application.version.number@

# [JWT expiration time in milliseconds]
auth.expirationTime=1200000

auth.secret=hygsecret
auth.authenticationProviders=STANDARD
```