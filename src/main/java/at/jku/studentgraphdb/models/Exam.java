package at.jku.studentgraphdb.models;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Exam")
public record Exam(
        @Id @Property("id") String id,
        String date,
        String room,
        String note) {
}
