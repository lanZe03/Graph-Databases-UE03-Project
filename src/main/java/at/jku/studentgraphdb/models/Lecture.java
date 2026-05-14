package at.jku.studentgraphdb.models;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Lecture")
public record Lecture(
        @Id @Property("id") String id,
        String topic,
        Integer ects
) {}
