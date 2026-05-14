package at.jku.studentgraphdb.repository;

import at.jku.studentgraphdb.models.Notification;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface NotificationRepository extends Neo4jRepository<Notification, Long> {

}
