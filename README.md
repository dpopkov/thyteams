# thyteams

### Init database
* Start Postgres in container: `docker compose up -d`
* Run application using profiles `local` and `init-db`

### Running application
* As a packaged application: `java -jar target/thyteams-0.0.1-SNAPSHOT`
* Using Maven Plugin: `mvn spring-boot:run`
