# ACME Air Flights Service

This project implements a REST API using Spring Boot to request flight information and manage bookings for ACME Air.

## Dependencies

* Java 21
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

3. Once the project dependencies are installed, run the application using the command below which will launch the app on this URL: `http://localhost:8080/`

```
./gradlew bootRun
```