# Simple Weather Information Service

This project implements a REST API using Spring Boot to request weather data (upto 3 cities in any given request).

## Dependencies

* Java 17
* Gradle 8.5
* Spring Data JPA
* Spring Starter Web
* H2 (in-memory database)
* Lombok

## Running the application

1. Clone this source repository into your chosen directory either by downloading this project as a zip or using the command below.

```
git clone https://github.com/arorahrsh/arorahr-tech-assessment.git
```

2. Change the current directory to be the cloned repository and install the project dependencies using `gradle`

```
cd arorahr-tech-assessment
./gradlew build
```

3. Once the project dependencies are installed, run the application using the command below which will deploy the app on `http://localhost:8080/`

```
./gradlew bootRun
```

4. Use CURL or a REST Client (e.g. Postman) to call this weather service API

```
curl -H 'Content-Type: application/json' -d '[{"city":"Auckland"},{"city":"Wellington"}]' -X POST http://localhost:8080/v1/weather
```
