# passport-management-system

Passport management system API. Implemented using Java, Spring Boot, PostgreSQL, Flyway, Docker. RestAssured and
TestContainers are used for testing.

OpenAPI specification can be found in the root directory of the project.

To start the app just do the following:

* Make sure you have Docker installed, configured, and running on your machine
* Set the DB password in `application.properties` (`spring.datasource.password`) and `docker-compose.yaml`
  files (`POSTGRES_PASSWORD` and `SPRING_DATASOURCE_PASSWORD`)
* Run `mvn clean install` from the root directory of the project
* Run `docker-compose up` from the root directory of the project
* The app should be up and running on port 8080
