FROM maven:3.5.2-jdk-8-alpine as BUILD
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app
RUN mvn clean install -Pdev -DskipTests

ARG DATABASE_HOST=127.0.0.1
ARG DATABASE_PORT=3306
ARG DATABASE_NAME=MarketGateway
 
 
FROM openjdk:8-jre-alpine
COPY --from=BUILD /app/target/*.jar  application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "-Djasypt.encryptor.password=KEYCODE", "/application.jar"]