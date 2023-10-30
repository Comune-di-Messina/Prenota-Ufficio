FROM openjdk:11-slim

ARG JAR_FILE=target/*.jar
RUN mkdir -p /config
ADD ${JAR_FILE} ./app.jar
ENTRYPOINT ["java","-jar","/app.jar"]