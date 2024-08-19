#FROM openjdk:17-jdk-slim
#VOLUME /tmp
#ARG JAVA_OPTS
#ENV JAVA_OPTS=$JAVA_OPTS
#COPY target/taskflow-0.0.1-SNAPSHOT.jar taskflow.jar
#EXPOSE 3000
#ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -jar taskflow.jar"]
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar taskflow.jar


FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install

FROM maven:3.9.4-eclipse-temurin-17
WORKDIR /app
COPY . .
CMD ["mvn", "spring-boot:run"]
EXPOSE 8080