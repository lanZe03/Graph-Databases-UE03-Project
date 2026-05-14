package at.jku.studentgraphdb.models;

import lombok.Builder;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Lecture")
@Builder
public record Lecture(
        @Id @Property("id") String id,
        String topic,
        Integer ects
) {
    @Override
    public String toString() {
        return this.topic + "(" + this.id + ", ECTS:" + this.ects + ")";
    }
}
