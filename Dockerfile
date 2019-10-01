FROM mysql:5.7 as builder
RUN ["sed", "-i", "s/exec \"$@\"/echo \"not running $@\"/", "/usr/local/bin/docker-entrypoint.sh"]

ENV MYSQL_ROOT_PASSWORD=tpm1234

COPY setup.sql /docker-entrypoint-initdb.d/
RUN ["/usr/local/bin/docker-entrypoint.sh", "mysqld", "--datadir", "/initialized-db"]

FROM mysql:5.7
COPY --from=builder /initialized-db /var/lib/mysql


FROM maven:3.5.2-jdk-8-alpine as BUILD
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app
RUN mvn clean install -Pdev -DskipTests


ARG DATABASE_HOST=mysql-container
ARG DATABASE_PORT=3306
ARG DATABASE_PORT_PROXY=33306
ARG DATABASE_NAME=MarketGateway
ARG DATABASE_USER=root
ARG DATABASE_PASSWORD=tpm1234
 
 
FROM openjdk:8-jre-alpine
COPY --from=BUILD /app/target/*.jar  application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "-Djasypt.encryptor.password=KEYCODE", "/application.jar"]