# Demo Shop

Demo Shop is a simple RESTfull application that demonstrates using Srping Boot and Swagger.  

Application stores the data withing an in-memory H2 database.

## Prerequisities

Before you begin, ensure you have the following:

- Java 11

## Building the app

Demo Shop is based on Gradle wrapper, so the following command will build the app and run the tests:
```bash
./gradlew build
```
In order to get the list of all available tasks:
```bash
./gradlew tasks --all
```

## Running the app

The project uses a Spring Boot gralde plugin which allows running it via following command:
```bash
./gradlew bootRun
```

Alternatively, you can generate the jar:
```bash
./gradlew bootJar
```

and run it via `java` command:
```bash
java -jar  java -jar build/libs/demo-shop-0.1-SNAPSHOT.jar
```

## API Docs

You can access the Swagger docs at http://localhost:8080/swagger-ui.html

## Database

You can access the H2 database by following this url: http://localhost:8080/h2-console
