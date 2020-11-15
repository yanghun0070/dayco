#FROM ubuntu:20.04
FROM adoptopenjdk/openjdk11
MAINTAINER yanghun0070@gmail.com
#RUN mkdir -p /www/
#ADD dayco-uaa-0.0.1-SNAPSHOT.jar /www/app.jar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9999
#ENTRYPOINT ["java", "-jar", "/www/app.jar"]
ENTRYPOINT ["java", "-jar", "/app.jar"]
