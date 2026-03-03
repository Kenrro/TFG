FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]