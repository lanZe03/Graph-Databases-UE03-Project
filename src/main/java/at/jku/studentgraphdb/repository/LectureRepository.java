package at.jku.studentgraphdb.repository;

import at.jku.studentgraphdb.models.Lecture;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends Neo4jRepository<Lecture, String> {
    Optional<Lecture> findById(String id);

    List<Lecture> findAllByOrderByTopicAsc();

    boolean existsById(String id);
}
