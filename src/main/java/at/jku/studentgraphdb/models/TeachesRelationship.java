package at.jku.studentgraphdb.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@RelationshipProperties
public class TeachesRelationship {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private Integer order;
    @Setter
    private String term;

    @TargetNode
    @Setter
    private Lecture lecture;

    public TeachesRelationship() {
    }

    public TeachesRelationship(Integer order, String term, Lecture lecture) {
        this.order = order;
        this.term = term;
        this.lecture = lecture;
    }
}