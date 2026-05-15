package at.jku.studentgraphdb.repository;

import at.jku.studentgraphdb.models.Lecture;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends Neo4jRepository<Lecture, String> {
    Optional<Lecture> findById(String id);

    List<Lecture> findAllByOrderByTopicAsc();

    boolean existsById(String id);

    // Search for specific lectures with on search string
    // just look through all related properties and search for any match
    @Query("""
            MATCH (l:Lecture)
            OPTIONAL MATCH (p:Professor)-[:TEACHES]->(l)
            OPTIONAL MATCH (l)-[:HAS_EXAM]->(e:Exam)
            WITH l, collect(DISTINCT p.name) AS professorNames, collect(DISTINCT e.room) AS rooms, collect(DISTINCT e.date) AS dates
            WHERE
                toLower(coalesce(l.id, '')) CONTAINS toLower($search)
                OR toLower(coalesce(l.topic, '')) CONTAINS toLower($search)
                OR ANY(name IN professorNames WHERE toLower(coalesce(name, '')) CONTAINS toLower($search))
                OR ANY(room IN rooms WHERE toLower(coalesce(room, '')) CONTAINS toLower($search))
                OR ANY(date IN dates WHERE toLower(coalesce(date, '')) CONTAINS toLower($search))
            RETURN l
            ORDER BY l.id ASC
            """)
    List<Lecture> searchLectures(@Param("search") String search);
}
