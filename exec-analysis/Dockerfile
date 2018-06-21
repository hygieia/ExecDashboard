FROM openjdk:8
VOLUME /var/app/config
ARG JAR_FILE
COPY ${JAR_FILE} /var/app/analysis.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/var/app/analysis.jar"]
CMD [ "--spring.config.location=/var/app/config/application.properties" ]