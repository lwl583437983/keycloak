FROM openjdk:17
LABEL authors="liaowl3"
LABEL name=remote-lock

COPY target/*.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "/app.jar"]