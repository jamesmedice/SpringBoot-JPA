FROM maven:3.6-jdk-8 as maven
WORKDIR /app
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src

RUN mvn clean install -DskipTests && cp target/*.jar app.jar

# Rely on Docker's multi-stage build to get a smaller image based on JRE
FROM openjdk:8-jre-alpine
LABEL maintainer="tiago.sllater@gmail.com"
WORKDIR /app
COPY --from=maven /app/app.jar ./app.jar


# VOLUME /tmp  # optional
EXPOSE 8181    # also optional

ENTRYPOINT ["java -jar","/app/app.jar"]