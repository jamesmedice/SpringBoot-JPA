# Spring DATA JPA Gateway 

This is a "uaa" application intended to be part of a microservice architecture, please refer to the [][] page of the documentation for more information.

This is also a JHipster User Account and Authentication (UAA) Server, refer to [Using UAA for Microservice Security][] for details on how to secure JHipster microservices with OAuth2.
This application is configured for Service Discovery and Configuration with . On launch, it will refuse to start if it is not able to connect to .

## Development

To start your application in the dev profile, simply run:

    ./mvn spring-boot:run -Dspring.profiles.active=dev -Djasypt.encryptor.password=encryptKeyCode


For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

 
## Continuous Integration (optional)
