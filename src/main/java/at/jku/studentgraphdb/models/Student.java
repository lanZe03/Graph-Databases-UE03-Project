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
@Node("Student")
public class Student {

    @Id
    @Property("matriculationNumber")
    private String matriculationNumber;

    private String name;

    @Relationship(type = "HEARS", direction = Relationship.Direction.OUTGOING)
    private Set<Lecture> lectures = new HashSet<>();

    @Relationship(type = "REGISTERS", direction = Relationship.Direction.OUTGOING)
    private Set<Exam> registeredExams = new HashSet<>();

    @Relationship(type = "HAS_GRADE", direction = Relationship.Direction.OUTGOING)
    private Set<StudentGrade> grades = new HashSet<>();

    public Student() {
    }

    public Student(String matriculationNumber, String name) {
        this.matriculationNumber = matriculationNumber;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name + "(" + matriculationNumber + ")";
    }
}