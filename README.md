# Spring DATA JPA Gateway 

This is a "uaa" application intended to be part of a microservice architecture, please refer to the [] page of the documentation for more information.

This is also a OAUTH2 Authentication (UAA) Server, refer to [Using UAA for Microservice Security] for details on how to secure microservices with OAuth2.
This application is configured for Service Discovery and Configuration with . On launch, it will refuse to start if it is not able to connect to .

## Development

To start your application in the dev profile, simply run:

    ./mvn spring-boot:run -Dspring.profiles.active=dev -Djasypt.encryptor.password=encryptKeyCode

 
## DOCKER


docker run -d -p 3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=tpm1234 -e MYSQL_DATABASE=MarketGateway mysql:latest

docker run -t  --link mysql-docker-container:mysql -p 8087:8080 jamesmedice/gateway