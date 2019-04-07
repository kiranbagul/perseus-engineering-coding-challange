# User Notification Platform 

Platform consists of following componants

- User service 
- Template service 
- Notification service
- Infrastructure
- Notification generator

## Summary

Each of User service, Template service and Notification service is standalone scala microservice integrated with docker container for deployement. Detailed information about each service is mentioned in respective repository. 

Infrastructure repository contains wrapper script for building all the services and publishing docker images along with docker-compose configuration to start all the services backed with mysql datastore. 

Notification generator contains scipts for sending newsletter and welcome email for new user.

## Setup required

- Java 8
- scala, sbt
- docker
- curl

## Installation

```
> cd Infrastructure
> build-all.sh 
> docker images
> docker-compose up
```
`build-all.sh` will run the tests, build artifact and generate docker images for all three services.

`docker images` command should list `notification-service`, `template-service`, `user-service`

`docker-compose up` will start the all the services and their database containers.

#### Sending the notifications
```
> cd notification-generator
> chmod +x send-newsletter.sh user-created.sh 
> ./user-created.sh 56
> ./send-newsletter.sh
```
`/user-created.sh USER_ID` will send notification to user with ID `USER_ID`
`./send-newsletter.sh` will send notifications to all users who have subscribed to newsletters
Email sent log can be verified on logs displayed on terminal where `docker-compose` is running under `notification-service` container
or with `docker-compose logs -f notification-service` from Infrastructure directory.

### Assumptions

- As per problem statement Users/templates collection is given, which is loaded when application is starting up. 
- For sending welome notification if ID is not within 42-64 as per collection provided, then error is returned.  

### Improvements
- Configuring service ports through properties yaml/configuration management.
- Kubernetes configuration to start Kubernetes cluster and setup environments
- Mysql : Using persistent volume conf and authentication, database containers are without credentials for demo purpose currently
- Automated API documentation  with swagger-ui integration
- Each service will go seperate repository, with CI/CD integrated
- Integration of pact framework, for consumer driven contracts and integration testing
- Better error message handling






 


