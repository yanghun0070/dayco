FROM adoptopenjdk/openjdk11
MAINTAINER yanghun0070@gmail.com
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "/app.jar"]
