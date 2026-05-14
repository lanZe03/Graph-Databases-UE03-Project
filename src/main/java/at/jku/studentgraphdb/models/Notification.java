package at.jku.studentgraphdb.models;

import lombok.Builder;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Notification")
@Builder
public record Notification (
        @Id @GeneratedValue Long id,
        String deletedEntityType,
        String deletedEntityId,
        String message,
        String reason,
        String deletedAt
) {}
