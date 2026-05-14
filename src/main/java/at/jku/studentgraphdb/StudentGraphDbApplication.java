package at.jku.studentgraphdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories(basePackages = "at.jku.studentgraphdb.repository")
// UE03, Graph DB, k12308938 - Stefan Lanzerstorfer
public class StudentGraphDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentGraphDbApplication.class, args);
    }

}
