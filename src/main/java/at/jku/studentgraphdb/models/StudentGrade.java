package at.jku.studentgraphdb.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@RelationshipProperties
public class StudentGrade {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private Integer grade;

    @Setter
    @TargetNode
    private Exam exam;

    public StudentGrade() {
    }

    public StudentGrade(Integer grade, Exam exam) {
        this.grade = grade;
        this.exam = exam;
    }

}