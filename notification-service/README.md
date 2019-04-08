# Notification service

Technologies/frameworks Used
- Scala
- Akka HTTP
- Scalatest
- Docker

### API

* `POST /v1/notifications/` to send newletter to all users who have subscribed to newsletters

* `POST /v1/notifications` with body containing `application/json {"ID":"<USER_ID>"}` to send welcome email to user with id <USER_ID>

* `GET /health` to monitor application status


#### `curl` equivalents
* welcome email
```
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"ID":"45"}' \
  http://localhost:9003/v1/notifications
```

* newsletter email
```
curl --request POST http://localhost:9003/v1/notifications/newsletter
```

## How to run

#### configuration
Base URLs of user and template service should be configured in application.conf located at src/main/resources
```
templateServiceUrl = "http://template-service:9001/v1/templates"
userServiceUrl = "http://user-service:9001/v1/users"
```

### build

```
> cd ci-scripts
> chmod +x build.sh
>./build.sh
```
Build script will publish a docker image for the service.
same can be executed with following command
```
> docker run -p 9003:9001 notification-service
```

