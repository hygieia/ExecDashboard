# API Configuration

Create a file in this folder named application.properties and enter the following properties:

```
dbname=analyticsdb
dbusername=analyticsuser
dbpassword=analyticspass
dbhost=exec-db
dbport=27017

portfolio.cron=0 */5 * * * *

portfolio.readUriUserName=dashboarduser
portfolio.readUriPassword=dbpassword

portfolio.readUriDatabase=db:27017
portfolio.readUriPrefix=mongodb
portfolio.readDatabase=dashboarddb

portfolio.incidentsCollectorFlag=true
portfolio.scmCollectorFlag=true
portfolio.codeAnalysisCollectorFlag=true

server.port=8081
```