package at.jku.studentgraphdb.repository;

import at.jku.studentgraphdb.dto.StudentExam;
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

    @Query("""
        MATCH (s:Student)
        WHERE toLower(s.name) CONTAINS toLower($search)
           OR toLower(s.matriculationNumber) CONTAINS toLower($search)
        RETURN s
        ORDER BY s.name ASC
        """)
    List<Student> searchStudents(@Param("search") String search);

    @Query("""
        MATCH (s:Student {matriculationNumber: $matriculationNumber})-[:REGISTERS]->(e:Exam)
        MATCH (l:Lecture)-[:HAS_EXAM]->(e)
        OPTIONAL MATCH (s)-[g:HAS_GRADE]->(e)
        RETURN
            l.id AS lectureId,
            l.topic AS lectureTopic,
            e.date AS examDate,
            e.room AS examRoom,
            g.grade AS currentGrade,
            (coalesce(l.id,'') + '|' + coalesce(e.date,'') + '|' + coalesce(e.room,'')) AS examKey
        ORDER BY l.id ASC, e.date ASC, e.room ASC
        """)
    List<StudentExam> findRegisteredExams(@Param("matriculationNumber") String matriculationNumber);

    @Query("""
        RETURN EXISTS(
            (:Student {matriculationNumber: $matriculationNumber})-[:REGISTERS]->
            (:Exam)<-[:HAS_EXAM]-(:Lecture {id: $lectureId})
        ) AS registered
        """)
    boolean isStudentRegisteredForLectureExam(@Param("matriculationNumber") String matriculationNumber,
                                              @Param("lectureId") String lectureId);

    @Query("""
        MATCH (s:Student {matriculationNumber: $matriculationNumber})-[:REGISTERS]->(e:Exam)
        MATCH (l:Lecture {id: $lectureId})-[:HAS_EXAM]->(e)
        WHERE coalesce(e.date,'') = coalesce($examDate,'')
          AND coalesce(e.room,'') = coalesce($examRoom,'')
        MERGE (s)-[g:HAS_GRADE]->(e)
        SET g.grade = $grade
        """)
    void saveGrade(@Param("matriculationNumber") String matriculationNumber,
                   @Param("lectureId") String lectureId,
                   @Param("examDate") String examDate,
                   @Param("examRoom") String examRoom,
                   @Param("grade") Long grade);

    @Query("""
        RETURN EXISTS(
            (:Student {name: $nameA})-[:HEARS]->(:Lecture)<-[:HEARS]-(:Student {name: $nameB})
        ) AS connected
        """)
    boolean areClassmates(@Param("nameA") String nameA, @Param("nameB") String nameB);
}
