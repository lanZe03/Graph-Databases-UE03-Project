package at.jku.studentgraphdb.repository;

import at.jku.studentgraphdb.models.Student;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends Neo4jRepository<Student, String> {
    Optional<Student> findByMatriculationNumber(String matriculationNumber);

    Optional<Student> findByName(String matriculationNumber);

    // used to show a list of students
    List<Student> findAllByOrderByNameAsc();

    @Query("""
            MATCH (s:Student {matriculationNumber: $matriculationNumber})
            DETACH DELETE s
            """)
    void deleteStudentByMatriculationNumber(@Param("matriculationNumber") String matriculationNumber);

    @Query("""
            MATCH (s:Student)-[:HEARS]->(l:Lecture {id: $lectureId})
            RETURN s
            ORDER BY s.name ASC
            """)
    List<Student> findParticipantsByLectureId(@Param("lectureId") String lectureId);

    @Query("""
            MATCH (s:Student {matriculationNumber: $matriculationNumber})
            MATCH (l:Lecture {id: $lectureId})
            MERGE (s)-[:HEARS]->(l)
            """)
    void addStudentToLecture(@Param("matriculationNumber") String matriculationNumber,
                             @Param("lectureId") String lectureId);

    @Query("""
            MATCH (s:Student {matriculationNumber: $matriculationNumber})-[r:HEARS]->(l:Lecture {id: $lectureId})
            DELETE r
            """)
    void removeStudentFromLecture(@Param("matriculationNumber") String matriculationNumber,
                                  @Param("lectureId") String lectureId);
}
