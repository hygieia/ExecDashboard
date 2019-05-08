FROM openjdk:8
VOLUME /var/app/config
ARG JAR_FILE
COPY ${JAR_FILE} /var/app/analysis.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /var/app/analysis.jar --spring.config.location=/var/app/config/application.properties