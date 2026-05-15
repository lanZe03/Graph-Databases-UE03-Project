# StudentGraphDB

A small Spring Boot project for working with a Neo4j-based student and university graph.

Created by Stefan Lanzerstorfer, k12308938 for the Course Graph Databases.

The project starts a neo4j instance in a docker container using the compose.yaml file.
This gets seeded with the given test-data on startup.

## Features

- Home page with quick access to all functions
- Delete students and create notifications
- Show lecture participants and update them
- Search lectures by different fields
- Grade students for exams
- Check connections between people
- Search paths between graph nodes
- View notifications


## Tech Stack

- Java
- Spring Boot
- Thymeleaf
- Neo4j
- Docker
- HTML/CSS

## Run the project

1. Install Docker and especially Docker Compose on the local system to be able to start the neo4j instance
2. Make sure Java and Maven are installed.
3. Start the application with:

```bash
mvn spring-boot:run
```
or open the StudentGraphDbApplication.java and press Start when using any modern IDE (IntelliJ.. )

4. Open the app in the browser:

```text
http://localhost:8080
```

## Project Structure

- `src/main/java` - Java source code
  - This follows a classic Spring Boot Setup 
    - Controllers in `src/main/java/at/jku/studentgraphdb/controller` for the REST Sites
    - Services in `src/main/java/at/jku/studentgraphdb/service` that provide functionality to the Controllers
    - Repositories in `src/main/java/at/jku/studentgraphdb/repository`that execute the Queries on the Neo4j instance.
    - Models in `src/main/java/at/jku/studentgraphdb/model` that represent all the Domain Objects like Students and Professors.
    - Dtos in `src/main/java/at/jku/studentgraphdb/dto` to define custom Data Transfer Objects for non-Domain entities.
    - Config in `src/main/java/at/jku/studentgraphdb/config` 
      - Here we define a CommandLineRunner DatabaseSeeder, which seeds the Database with some test-data
      - GlobalModelAttributes which can be used in the Frontend in all Pages, to make passing those few models easier.
  - We use Data-neo4j for interfacing with the Database
- `src/main/resources/templates` - Thymeleaf templates
- `src/main/resources/static/css` - CSS files
- `src/main/resources/static` - static assets