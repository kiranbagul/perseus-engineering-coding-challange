# Template service


### API

* `GET /v1/templates/id/<TEMPLATE_ID>` to get template info of by template id Ex. `/v1/templates/2`

Response
```
{
  "id": 2,
  "name": "welcome",
  "body": "Hello dear {{user.salutation}} {{user.name}},we are very happy to welcome you to our newsletter.In case you don't want to receive any further newsletters in the future please unsubscribe here:https://domain-of-product.de/unsubscribe-newsletter/{{user.identifier}}Best Regards,Your Customer Support Team"
}
```

* `GET /v1/templates/welcome` to get welcome template
* `GET /v1/templates/newsletter` to get newsletter template


Response
```
{
  "id": 1,
  "name": "newsletter",
  "body": "Hello dear {{user.salutation}} {{user.name}},this are our latest news...In case you don't want to receive any further newsletters in the future please unsubscribe here:https://domain-of-product.de/unsubscribe-newsletter/{{user.identifier}}Best Regards,Your Customer Support Team"
}
```


## How to run

#### configuration
Template service can be started in two modes

1. in-memory backed by collection in JSON file
2. MySql database
Followinf configuration is located at src/main/resources

```

db {
  in-memory = false
  sql = {
    user = "root"
    password = ""
    url = "jdbc:mysql://template-service-database:3306/user-service?autoReconnect=true&useSSL=false&characterEncoding=utf8&useUnicode=true"
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
> docker run -p 9001:9001 template-service
```

