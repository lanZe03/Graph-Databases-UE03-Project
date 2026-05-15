package at.jku.studentgraphdb.repository;

import at.jku.studentgraphdb.models.Professor;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessorRepository extends Neo4jRepository<Professor, String> {
    @Query("""
            RETURN EXISTS(
                (:Professor {name: $nameA})-[:TEACHES]->(:Lecture)<-[:TEACHES]-(:Professor {name: $nameB})
            ) AS connected
            """)
    boolean areColleagues(@Param("nameA") String nameA, @Param("nameB") String nameB);
}
