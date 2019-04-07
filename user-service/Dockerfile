FROM openjdk:8-jre-alpine

WORKDIR /app
COPY target/scala-2.12/*.jar    /app/

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.2.1/wait /wait
RUN chmod +x /wait

CMD /wait && java -jar user-service-assembly-0.0.1.jar