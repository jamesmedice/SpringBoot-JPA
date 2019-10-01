# Spring DATA JPA Gateway 

This is a "uaa" application intended to be part of a microservice architecture, please refer to the [] page of the documentation for more information.

This is also a OAUTH2 Authentication (UAA) Server, refer to [Using UAA for Microservice Security] for details on how to secure microservices with OAuth2.
This application is configured for Service Discovery and Configuration with . On launch, it will refuse to start if it is not able to connect to .

## Development

To start your application in the dev profile, simply run:

    ./mvn spring-boot:run -Dspring.profiles.active=dev -Djasypt.encryptor.password=encryptKeyCode

 
## DOCKER
## remova all containers
docker rm `docker ps -aq`

docker pull mysql

docker run -p 33306:3306 --name mysql-container -e MYSQL_ROOT_PASSWORD=xxxx -e MYSQL_DATABASE=MarketGateway mysql:5.7
 

docker run --name applicationBootName -d --link mysql-container:db -p 8585:8585  <image app id>


