# User service

Technologies/frameworks Used
- Scala
- Akka HTTP
- Slick
- MySql
- H2 in memory DB (for testing)
- Scalatest
- Docker

### API

* `GET /v1/users/<USER_ID>` to get user info of for user id <USER_ID> Ex. `/v1/users/45`

Response
 ```
    {
      "id": 42,
      "surName": "Turner",
      "firstName": "Tom",
      "gender": "male",
      "email": "tom.turner@provider.de",
      "subscribedNewsletter": true
    }
  ```


* `GET /v1/users` to get all user information

Response
  ```
  [
    {
      "id": 42,
      "surName": "Turner",
      "firstName": "Tom",
      "gender": "male",
      "email": "tom.turner@provider.de",
      "subscribedNewsletter": true
    },
    {
      "id": 43,
      "surName": "Doe",
      "firstName": "John",
      "gender": "male",
      "email": "jon.doe@test-mailing.com",
      "subscribedNewsletter": true
    }
  ]
  ```

* `GET /health` to monitor application status

## How to run

#### configuration
User service can be started in two modes

1. in-memory backed by collection in JSON file
2. MySql database
Followinf configuration is located at src/main/resources

```

db {
  in-memory = false
  sql = {
    user = "root"
    password = ""
    url = "jdbc:mysql://user-service-database:3306/user-service?autoReconnect=true&useSSL=false&characterEncoding=utf8&useUnicode=true"
    driver = "com.mysql.jdbc.Driver"
    connectionPool = disabled
    keepAliveConnection = true
  }
}
```


- set `in-memory = true` for in-memory mode
- for MySql datastore mode set `in-memory = false` and update `user` `password` and `url` information.

### build

```
> cd ci-scripts
> chmod +x build.sh
>./build.sh
```
Build script will publish a docker image for the service.
same can be executed with following command
```
> docker run -p 9001:9001 user-service
```

