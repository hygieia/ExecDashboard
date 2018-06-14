FROM openjdk:8-jdk-alpine
VOLUME /var/app/config
ARG JAR_FILE
COPY ${JAR_FILE} /var/app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/var/app/app.jar"]
CMD [ "--spring.config.location=/var/app/config/api.properties" ]