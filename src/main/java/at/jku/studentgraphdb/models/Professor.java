package at.jku.studentgraphdb.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Node("Professor")
public class Professor {

    @Id
    @Property("employeeNumber")
    private String employeeNumber;

    private String name;

    @Relationship(type = "TEACHES", direction = Relationship.Direction.OUTGOING)
    private Set<TeachesRelationship> teaches = new HashSet<>();

    @Relationship(type = "EXAMINES", direction = Relationship.Direction.OUTGOING)
    private Set<Exam> examinedExams = new HashSet<>();

    public Professor() {
    }

    public Professor(String employeeNumber, String name) {
        this.employeeNumber = employeeNumber;
        this.name = name;
    }

}