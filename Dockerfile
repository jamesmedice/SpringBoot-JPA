FROM maven:3.6-jdk-8 as maven  openjdk:8-jre-alpine
WORKDIR /app
COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn clean install -Pdev -DskipTests

ARG DATABASE_HOST=127.0.0.1
ARG DATABASE_PORT=3306
ARG DATABASE_NAME=MarketGateway

VOLUME /tmp
COPY /target/*.jar  app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "-Djasypt.encryptor.password=KEYCODE", "/app.jar"]