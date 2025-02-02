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

3. Run the application using the command below which will deploy the app on `http://localhost:8080/`

```
./gradlew bootRun
```

4. Use CURL or a REST Client (e.g. Postman) to call this weather service API

```
curl -H 'Content-Type: application/json' -d '[{"city":"Auckland"},{"city":"Wellington"}]' -X POST http://localhost:8080/v1/weather
```

### Sample response

```json
[
  {
    "city": "Auckland",
    "temp": 30,
    "unit": "C",
    "date": "01/02/2025",
    "weather": "rain"
  },
  {
    "city": "Wellington",
    "temp": 39,
    "unit": "C",
    "date": "01/02/2025",
    "weather": "cloudy"
  }
]
```

### Sample logs

```
2025-02-01T23:17:16.841+13:00  INFO 11224 --- [nio-8080-exec-4] n.c.w.swis.controller.WeatherController  : Received POST /v1/weather request with JSON payload: [{"city":"Auckland"},{"city":"Wellington"}]
2025-02-01T23:17:16.841+13:00  INFO 11224 --- [nio-8080-exec-4] n.c.w.swis.service.WeatherServiceImpl    : Searching if cached weather data exists for city 'Auckland'
2025-02-01T23:17:16.842+13:00  INFO 11224 --- [nio-8080-exec-4] n.c.w.swis.service.WeatherServiceImpl    : Cached weather data not found in database for city 'Auckland'
2025-02-01T23:17:16.843+13:00  INFO 11224 --- [nio-8080-exec-4] n.c.w.swis.service.WeatherServiceImpl    : Requesting weather data from external mock service for city 'Auckland'
2025-02-01T23:17:16.863+13:00  INFO 11224 --- [nio-8080-exec-5] n.c.w.s.c.MockedWeatherInfoController    : Received GET /mocked-service/weather-info/Auckland
2025-02-01T23:17:16.864+13:00  INFO 11224 --- [nio-8080-exec-5] n.c.w.s.c.MockedWeatherInfoController    : Returning weather data from external API: {"city":"Auckland","temp":30,"unit":"C","date":"01/02/2025","weather":"rain"}
2025-02-01T23:17:16.868+13:00  INFO 11224 --- [nio-8080-exec-4] n.c.w.swis.service.WeatherServiceImpl    : Searching if cached weather data exists for city 'Wellington'
2025-02-01T23:17:16.873+13:00  INFO 11224 --- [nio-8080-exec-4] n.c.w.swis.service.WeatherServiceImpl    : Found cached weather data for city 'Wellington': {"city":"Wellington","temp":39,"unit":"C","date":"01/02/2025","weather":"cloudy"}
2025-02-01T23:17:16.873+13:00  INFO 11224 --- [nio-8080-exec-4] n.c.w.swis.controller.WeatherController  : Returning 200 HTTP status code and JSON response: [{"city":"Auckland","temp":30,"unit":"C","date":"01/02/2025","weather":"rain"},{"city":"Wellington","temp":39,"unit":"C","date":"01/02/2025","weather":"cloudy"}]
```

## High level design

This project is designed as a REST API that contains a public `POST /v1/weather` endpoint which returns weather data for the given cities. The API maintains an in-memory database of cached weather data. If the requested city is not available in cache, it calls a mocked external weather API to retrieve the information and persists this in the cache for subsequent requests.

While the external API is shown as a separate component in the design below, from an implementation perspective, this is implemented as a hidden `GET` endpoint in the Simple Weather Information Service.

![design](./images/02-design.png)

## API Specification

The project contains an Open API specification for this service which was drafted prior to development. This can be viewed on the [Swagger editor](https://editor.swagger.io/). Alternatively, a screenshot of the specification is shown below. For the raw specification, please refer to [api-spec.yaml](./api-spec.yaml).

![spec](./images/01-api-specification.png)

## Design considerations

* The project is structured into readable and maintainable packages with the key layers being `Controller`, `Repository` and `Service`.
* Services are hidden behind an interface definition to allow future extensibility of the project.
* The `application.properties` file is read using a `AppProperties` class to allow dependency injection and customisation of this application in different environments.
* No error validation is implemented for the mock external `GET` endpoint as all requests coming from the Simple Weather Information service (`POST` endpoint) are already validated before invoking the mock API.
* The mock external `GET` endpoint is purposely not documented in the Open API specification as this is a hidden endpoint that is only supposed to be used by the `POST` endpoint.
* The H2 library and Spring JPA were used to implement an in-memory cache while the application is running to persist weather information.
* A basic GitHub Actions CI pipeline was implemented to allow continuous build and test of the application. This pipeline runs on all branches including `master`.
* Verbose logging has been added as part of this API implementation. The default logging level is set to `INFO`.


## Assumptions

* City names are not validated to be correct city names.
* City names less than 2 characters are considered to be invalid.
* At least one and no more than 3 cities must be provided in the request JSON payload for the `POST` endpoint.
* No authentication or security checks (e.g. API keys or tokens) have been implemented as part of this API.
* The service returns the weather information from the in-memory cache if available. The implementation does not check if the weather data in the cache is for the current date. This is by design as it is assumed that the application will be restarted every day to reset the cache if this application were deployed into higher environments e.g. stress, ppte or production.
* Explicit error handling for 404 and 500 HTTP status codes is not implemented in this MVP implementation. However, these are still documented in the API specification for future implementation.