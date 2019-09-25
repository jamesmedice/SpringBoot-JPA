FROM maven:3.6-jdk-8 as maven
WORKDIR /app
COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn clean install -Pdev -DskipTests

FROM openjdk:8-jre-alpine
VOLUME /tmp
COPY /target/*.jar  app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "-Djasypt.encryptor.password=KEYCODE", "/app.jar"]