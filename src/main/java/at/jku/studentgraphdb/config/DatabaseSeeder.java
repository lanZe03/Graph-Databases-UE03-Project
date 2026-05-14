package at.jku.studentgraphdb.config;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Runs once after the Spring context starts.
 * Seeds the Neo4j database with the university data.
 */
@Component
public class DatabaseSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final Driver driver;

    public DatabaseSeeder(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void run(ApplicationArguments args) {
        try (Session session = driver.session()) {

            // Skip seeding if data already present
            long studentCount = session.run("MATCH (s:Student) RETURN count(s) AS c")
                    .single().get("c").asLong();
            if (studentCount > 0) {
                log.info("Database already seeded ({} students found). Skipping.", studentCount);
            } else {
                seedDatabase(session);
            }

            log.info("View the the project at http://localhost:8080");
            log.info("View the neo4j browser at http://localhost:7474/browser/");
            log.info("Password is secret123");
        }
    }

    private void seedDatabase(Session session) {
        log.info("Seeding Neo4j database...");

        // Students
        session.run("""
                CREATE
                  (s1:Student {name: 'Luca',   matriculationNumber: 'k12355001'}),
                  (s2:Student {name: 'Elvin',  matriculationNumber: 'k12355002'}),
                  (s3:Student {name: 'Marian', matriculationNumber: 'k12355003'}),
                  (s4:Student {name: 'Sam',    matriculationNumber: 'k12355004'}),
                  (s5:Student {name: 'Kim',    matriculationNumber: 'k12355005'}),
                  (s6:Student {name: 'Alex',   matriculationNumber: 'k12355006'}),
                  (s7:Student {name: 'Han',    matriculationNumber: 'k12355007'}),
                  (s8:Student {name: 'Robin',  matriculationNumber: 'k12355008'})
                """);

        // Professors
        session.run("""
                CREATE
                  (p1:Professor {name: 'Josef',    employeeNumber: 'ak110001'}),
                  (p2:Professor {name: 'Rene',     employeeNumber: 'ak110002'}),
                  (p3:Professor {name: 'Martina',  employeeNumber: 'ak110003'}),
                  (p4:Professor {name: 'Wolfram',  employeeNumber: 'ak110004'}),
                  (p5:Professor {name: 'Birgit',   employeeNumber: 'ak110005'}),
                  (p6:Professor {name: 'Richard',  employeeNumber: 'ak110006'}),
                  (p7:Professor {name: 'Michael',  employeeNumber: 'ak110007'}),
                  (p8:Professor {name: 'Gabriele', employeeNumber: 'ak110008'}),
                  (p9:Professor {name: 'Stefan',   employeeNumber: 'ak110009'})
                """);

        // Lectures
        session.run("""
                CREATE
                  (:Lecture {topic: 'Information Systems 1',       id: '351.011', ects: 3}),
                  (:Lecture {topic: 'Introduction to IT Security', id: '353.067', ects: 3}),
                  (:Lecture {topic: 'Betriebssysteme',             id: '353.006', ects: 3}),
                  (:Lecture {topic: 'Quantum Computing',           id: '336.058', ects: 3}),
                  (:Lecture {topic: 'Graph Databases',             id: '351.039', ects: 3}),
                  (:Lecture {topic: 'Formal Models',               id: '338.010', ects: 3}),
                  (:Lecture {topic: 'Computability and Complexity',id: '336.001', ects: 3}),
                  (:Lecture {topic: 'Multimedia Systems',          id: '367.060'})
                """);

        // TEACHES
        session.run("""
                MATCH (p1:Professor {name:'Josef'}),   (l1:Lecture {id:'351.011'})
                MATCH (p4:Professor {name:'Wolfram'}), (l1b:Lecture{id:'351.011'})
                MATCH (p2:Professor {name:'Rene'}),    (l2:Lecture {id:'353.067'})
                MATCH (p2b:Professor{name:'Rene'}),    (l3:Lecture {id:'353.006'})
                MATCH (p9:Professor {name:'Stefan'}),  (l3b:Lecture{id:'353.006'})
                MATCH (p7:Professor {name:'Michael'}), (l3c:Lecture{id:'353.006'})
                MATCH (p6:Professor {name:'Richard'}), (l4:Lecture {id:'336.058'})
                MATCH (p1c:Professor{name:'Josef'}),   (l5:Lecture {id:'351.039'})
                MATCH (p3:Professor {name:'Martina'}), (l6:Lecture {id:'338.010'})
                MATCH (p6b:Professor{name:'Richard'}), (l7:Lecture {id:'336.001'})
                MATCH (p8:Professor {name:'Gabriele'}),(l8:Lecture {id:'367.060'})
                CREATE
                  (p1) -[:TEACHES {order:1, term:'WS26'}]->(l1),
                  (p4) -[:TEACHES {order:2, term:'WS26'}]->(l1b),
                  (p2) -[:TEACHES {order:1, term:'WS26'}]->(l2),
                  (p2b)-[:TEACHES {order:1, term:'SS26'}]->(l3),
                  (p9) -[:TEACHES {order:2, term:'SS26'}]->(l3b),
                  (p7) -[:TEACHES {order:1, term:'WS26'}]->(l3c),
                  (p6) -[:TEACHES {order:1, term:'WS26'}]->(l4),
                  (p1c)-[:TEACHES {order:1, term:'SS26'}]->(l5),
                  (p3) -[:TEACHES {order:1, term:'SS26'}]->(l6),
                  (p6b)-[:TEACHES {order:1, term:'SS26'}]->(l7),
                  (p8) -[:TEACHES {order:1, term:'SS26'}]->(l8)
                """);

        // Professor -> Examines -> Exam
        session.run("""
                MATCH (l1:Lecture{id:'351.011'})
                MATCH (p1:Professor{name:'Josef'}),
                      (p4:Professor{name:'Wolfram'}),
                      (p5:Professor{name:'Birgit'})
                CREATE
                  (p1)-[:EXAMINES]->(e1:Exam{date:'2026-01-20', note:'First exam',            room:'HS 10'})<-[:HAS_EXAM]-(l1),
                  (p4)-[:EXAMINES]->(e2:Exam{date:'2026-01-20', note:'First exam',            room:'HS 16'})<-[:HAS_EXAM]-(l1),
                  (p5)-[:EXAMINES]->(e3:Exam{date:'2026-01-20', note:'First exam',            room:'HS 9' })<-[:HAS_EXAM]-(l1),
                  (p1)-[:EXAMINES]->(e4:Exam{date:'2026-03-28', note:'Second exam - canceled',room:'HS 2' })<-[:HAS_EXAM]-(l1),
                  (p1)-[:EXAMINES]->(e5:Exam{date:'2026-04-18', note:'Second exam',           room:'HS 1' })<-[:HAS_EXAM]-(l1)
                """);

        session.run("""
                MATCH (l2:Lecture{id:'353.067'}), (p2:Professor{name:'Rene'})
                CREATE (p2)-[:EXAMINES]->(e:Exam{date:'2026-03-30', room:'S3 055'})<-[:HAS_EXAM]-(l2)
                """);

        session.run("""
                MATCH (l3:Lecture{id:'353.006'})
                MATCH (p2:Professor{name:'Rene'}), (p7:Professor{name:'Michael'})
                CREATE
                  (p2)-[:EXAMINES]->(ea:Exam{date:'2026-06-18', note:'First exam room 1', room:'HS 1' })<-[:HAS_EXAM]-(l3),
                  (p2)-[:EXAMINES]->(eb:Exam{date:'2026-06-18', note:'First exam room 2', room:'HS 16'})<-[:HAS_EXAM]-(l3),
                  (p7)-[:EXAMINES]->(ec:Exam{date:'2026-01-18', note:'First exam room 3', room:'HS 2' })<-[:HAS_EXAM]-(l3),
                  (p7)-[:EXAMINES]->(ed:Exam{date:'2026-01-18', note:'First exam room 4', room:'HS 9' })<-[:HAS_EXAM]-(l3)
                """);

        session.run("""
                MATCH (l4:Lecture{id:'336.058'}), (p6:Professor{name:'Richard'})
                CREATE (p6)-[:EXAMINES]->(e:Exam{date:'2026-02-06', room:'HS 7'})<-[:HAS_EXAM]-(l4)
                """);

        session.run("""
                MATCH (l5:Lecture{id:'351.039'}), (p1:Professor{name:'Josef'})
                CREATE (p1)-[:EXAMINES]->(e:Exam{date:'2026-06-26'})<-[:HAS_EXAM]-(l5)
                """);
        session.run("""
                MATCH (l6:Lecture{id:'338.010'}), (p3:Professor{name:'Martina'})
                CREATE (p3)-[:EXAMINES]->(e:Exam{date:'2026-06-30'})<-[:HAS_EXAM]-(l6)
                """);
        session.run("""
                MATCH (l7:Lecture{id:'336.001'}), (p6:Professor{name:'Richard'})
                CREATE (p6)-[:EXAMINES]->(e:Exam{date:'2026-06-30'})<-[:HAS_EXAM]-(l7)
                """);
        session.run("""
                MATCH (l8:Lecture{id:'367.060'}), (p8:Professor{name:'Gabriele'})
                CREATE
                  (p8)-[:EXAMINES]->(e1:Exam{date:'2026-06-30'})<-[:HAS_EXAM]-(l8),
                  (p8)-[:EXAMINES]->(e2:Exam{date:'2026-10-15'})<-[:HAS_EXAM]-(l8)
                """);

        //  Student → HEARS -> Lecture
        session.run("""
                MATCH (s:Student {matriculationNumber:'k12355001'})
                MATCH (l1:Lecture{id:'351.011'}),(l2:Lecture{id:'353.067'}),
                      (l3:Lecture{id:'353.006'}),(l4:Lecture{id:'336.058'})
                CREATE (s)-[:HEARS]->(l1),(s)-[:HEARS]->(l2),
                       (s)-[:HEARS]->(l3),(s)-[:HEARS]->(l4)
                """);
        session.run("""
                MATCH (s:Student {matriculationNumber:'k12355002'})
                MATCH (l1:Lecture{id:'351.011'}),(l2:Lecture{id:'353.067'}),
                      (l3:Lecture{id:'353.006'}),(l4:Lecture{id:'336.058'}),
                      (l5:Lecture{id:'351.039'}),(l6:Lecture{id:'338.010'}),
                      (l7:Lecture{id:'336.001'}),(l8:Lecture{id:'367.060'})
                CREATE (s)-[:HEARS]->(l1),(s)-[:HEARS]->(l2),(s)-[:HEARS]->(l3),
                       (s)-[:HEARS]->(l4),(s)-[:HEARS]->(l5),(s)-[:HEARS]->(l6),
                       (s)-[:HEARS]->(l7),(s)-[:HEARS]->(l8)
                """);
        session.run("""
                MATCH (s:Student {matriculationNumber:'k12355004'})
                MATCH (l5:Lecture{id:'351.039'}),(l6:Lecture{id:'338.010'}),
                      (l7:Lecture{id:'336.001'}),(l8:Lecture{id:'367.060'})
                CREATE (s)-[:HEARS]->(l5),(s)-[:HEARS]->(l6),
                       (s)-[:HEARS]->(l7),(s)-[:HEARS]->(l8)
                """);
        session.run("""
                MATCH (s:Student {matriculationNumber:'k12355005'})
                MATCH (l1:Lecture{id:'351.011'}),(l3:Lecture{id:'353.006'}),
                      (l5:Lecture{id:'351.039'}),(l7:Lecture{id:'336.001'})
                CREATE (s)-[:HEARS]->(l1),(s)-[:HEARS]->(l3),
                       (s)-[:HEARS]->(l5),(s)-[:HEARS]->(l7)
                """);
        session.run("""
                MATCH (s:Student {matriculationNumber:'k12355006'})
                MATCH (l2:Lecture{id:'353.067'}),(l4:Lecture{id:'336.058'}),
                      (l6:Lecture{id:'338.010'}),(l8:Lecture{id:'367.060'})
                CREATE (s)-[:HEARS]->(l2),(s)-[:HEARS]->(l4),
                       (s)-[:HEARS]->(l6),(s)-[:HEARS]->(l8)
                """);
        session.run("""
                MATCH (s:Student {matriculationNumber:'k12355007'})
                MATCH (l1:Lecture{id:'351.011'}),(l2:Lecture{id:'353.067'}),
                      (l7:Lecture{id:'336.001'}),(l8:Lecture{id:'367.060'})
                CREATE (s)-[:HEARS]->(l1),(s)-[:HEARS]->(l2),
                       (s)-[:HEARS]->(l7),(s)-[:HEARS]->(l8)
                """);
        session.run("""
                MATCH (s:Student {matriculationNumber:'k12355008'})
                MATCH (l3:Lecture{id:'353.006'}),(l4:Lecture{id:'336.058'}),
                      (l5:Lecture{id:'351.039'}),(l6:Lecture{id:'338.010'})
                CREATE (s)-[:HEARS]->(l3),(s)-[:HEARS]->(l4),
                       (s)-[:HEARS]->(l5),(s)-[:HEARS]->(l6)
                """);

        // Student -> HAS_EXAM -> Registers -> Has_GRADE
        // s1 (Luca)
        session.run("""
                MATCH (s:Student{matriculationNumber:'k12355001'})
                MATCH (l1:Lecture{id:'351.011'})-[:HAS_EXAM]->(e1:Exam{room:'HS 10',  date:'2026-01-20'})
                MATCH (l1)                      -[:HAS_EXAM]->(e4:Exam{room:'HS 2',   date:'2026-03-28'})
                MATCH (l1)                      -[:HAS_EXAM]->(e5:Exam{room:'HS 1',   date:'2026-04-18'})
                MATCH (l3:Lecture{id:'353.006'})-[:HAS_EXAM]->(e3a:Exam{room:'HS 1',  date:'2026-06-18'})
                MATCH (l4:Lecture{id:'336.058'})-[:HAS_EXAM]->(e4a:Exam{room:'HS 7'})
                CREATE
                  (s)-[:REGISTERS]->(e1), (s)-[:HAS_GRADE{grade:5}]->(e1),
                  (s)-[:REGISTERS]->(e4),
                  (s)-[:REGISTERS]->(e5), (s)-[:HAS_GRADE{grade:1}]->(e5),
                  (s)-[:REGISTERS]->(e3a),(s)-[:HAS_GRADE{grade:2}]->(e3a),
                  (s)-[:REGISTERS]->(e4a),(s)-[:HAS_GRADE{grade:3}]->(e4a)
                """);

        // s2 (Elvin)
        session.run("""
                MATCH (s:Student{matriculationNumber:'k12355002'})
                MATCH (l1:Lecture{id:'351.011'})-[:HAS_EXAM]->(e4:Exam{room:'HS 2',   date:'2026-03-28'})
                MATCH (l1)                      -[:HAS_EXAM]->(e5:Exam{room:'HS 1',   date:'2026-04-18'})
                MATCH (l3:Lecture{id:'353.006'})-[:HAS_EXAM]->(e3d:Exam{room:'HS 9',  date:'2026-01-18'})
                MATCH (l4:Lecture{id:'336.058'})-[:HAS_EXAM]->(e4a:Exam{room:'HS 7'})
                MATCH (l5:Lecture{id:'351.039'})-[:HAS_EXAM]->(e5a:Exam{date:'2026-06-26'})
                MATCH (l6:Lecture{id:'338.010'})-[:HAS_EXAM]->(e6a:Exam{date:'2026-06-30'})
                MATCH (l7:Lecture{id:'336.001'})-[:HAS_EXAM]->(e7a:Exam{date:'2026-06-30'})
                MATCH (l8:Lecture{id:'367.060'})-[:HAS_EXAM]->(e8a:Exam{date:'2026-06-30'})
                CREATE
                  (s)-[:REGISTERS]->(e4),
                  (s)-[:REGISTERS]->(e5), (s)-[:HAS_GRADE{grade:1}]->(e5),
                  (s)-[:REGISTERS]->(e3d),(s)-[:HAS_GRADE{grade:1}]->(e3d),
                  (s)-[:REGISTERS]->(e4a),(s)-[:HAS_GRADE{grade:1}]->(e4a),
                  (s)-[:REGISTERS]->(e5a),(s)-[:HAS_GRADE{grade:1}]->(e5a),
                  (s)-[:REGISTERS]->(e6a),(s)-[:HAS_GRADE{grade:1}]->(e6a),
                  (s)-[:REGISTERS]->(e7a),(s)-[:HAS_GRADE{grade:1}]->(e7a),
                  (s)-[:REGISTERS]->(e8a),(s)-[:HAS_GRADE{grade:1}]->(e8a)
                """);

        // s4 (Sam)
        session.run("""
                MATCH (s:Student{matriculationNumber:'k12355004'})
                MATCH (l5:Lecture{id:'351.039'})-[:HAS_EXAM]->(e5a:Exam{date:'2026-06-26'})
                MATCH (l6:Lecture{id:'338.010'})-[:HAS_EXAM]->(e6a:Exam{date:'2026-06-30'})
                MATCH (l7:Lecture{id:'336.001'})-[:HAS_EXAM]->(e7a:Exam{date:'2026-06-30'})
                MATCH (l8:Lecture{id:'367.060'})-[:HAS_EXAM]->(e8a:Exam{date:'2026-06-30'})
                CREATE
                  (s)-[:REGISTERS]->(e5a),(s)-[:HAS_GRADE{grade:2}]->(e5a),
                  (s)-[:REGISTERS]->(e6a),(s)-[:HAS_GRADE{grade:1}]->(e6a),
                  (s)-[:REGISTERS]->(e7a),(s)-[:HAS_GRADE{grade:2}]->(e7a),
                  (s)-[:REGISTERS]->(e8a)
                """);

        // s5 (Kim)
        session.run("""
                MATCH (s:Student{matriculationNumber:'k12355005'})
                MATCH (l1:Lecture{id:'351.011'})-[:HAS_EXAM]->(e4:Exam{room:'HS 2',  date:'2026-03-28'})
                MATCH (l1)                      -[:HAS_EXAM]->(e5:Exam{room:'HS 1',  date:'2026-04-18'})
                MATCH (l5:Lecture{id:'351.039'})-[:HAS_EXAM]->(e5a:Exam{date:'2026-06-26'})
                CREATE
                  (s)-[:REGISTERS]->(e4),
                  (s)-[:REGISTERS]->(e5), (s)-[:HAS_GRADE{grade:3}]->(e5),
                  (s)-[:REGISTERS]->(e5a),(s)-[:HAS_GRADE{grade:3}]->(e5a)
                """);

        // s6 (Alex)
        session.run("""
                MATCH (s:Student{matriculationNumber:'k12355006'})
                MATCH (l3:Lecture{id:'353.006'})-[:HAS_EXAM]->(e3c:Exam{room:'HS 2', date:'2026-01-18'})
                MATCH (l4:Lecture{id:'336.058'})-[:HAS_EXAM]->(e4a:Exam{room:'HS 7'})
                MATCH (l6:Lecture{id:'338.010'})-[:HAS_EXAM]->(e6a:Exam{date:'2026-06-30'})
                MATCH (l8:Lecture{id:'367.060'})-[:HAS_EXAM]->(e8a:Exam{date:'2026-06-30'})
                CREATE
                  (s)-[:REGISTERS]->(e3c),(s)-[:HAS_GRADE{grade:4}]->(e3c),
                  (s)-[:REGISTERS]->(e4a),(s)-[:HAS_GRADE{grade:3}]->(e4a),
                  (s)-[:REGISTERS]->(e6a),(s)-[:HAS_GRADE{grade:1}]->(e6a),
                  (s)-[:REGISTERS]->(e8a),(s)-[:HAS_GRADE{grade:1}]->(e8a)
                """);

        // s7 (Han)
        session.run("""
                MATCH (s:Student{matriculationNumber:'k12355007'})
                MATCH (l1:Lecture{id:'351.011'})-[:HAS_EXAM]->(e4:Exam{room:'HS 2',   date:'2026-03-28'})
                MATCH (l1)                      -[:HAS_EXAM]->(e5:Exam{room:'HS 1',   date:'2026-04-18'})
                MATCH (l3:Lecture{id:'353.006'})-[:HAS_EXAM]->(e3b:Exam{room:'HS 16', date:'2026-06-18'})
                MATCH (l8:Lecture{id:'367.060'})-[:HAS_EXAM]->(e8a:Exam{date:'2026-06-30'})
                MATCH (l8)                      -[:HAS_EXAM]->(e8b:Exam{date:'2026-10-15'})
                CREATE
                  (s)-[:REGISTERS]->(e4),
                  (s)-[:REGISTERS]->(e5), (s)-[:HAS_GRADE{grade:2}]->(e5),
                  (s)-[:REGISTERS]->(e3b),(s)-[:HAS_GRADE{grade:1}]->(e3b),
                  (s)-[:REGISTERS]->(e8a),(s)-[:HAS_GRADE{grade:3}]->(e8a),
                  (s)-[:REGISTERS]->(e8b),(s)-[:HAS_GRADE{grade:3}]->(e8b)
                """);

        // s8 (Robin)
        session.run("""
                MATCH (s:Student{matriculationNumber:'k12355008'})
                MATCH (l4:Lecture{id:'336.058'})-[:HAS_EXAM]->(e4a:Exam{room:'HS 7'})
                MATCH (l5:Lecture{id:'351.039'})-[:HAS_EXAM]->(e5a:Exam{date:'2026-06-26'})
                CREATE
                  (s)-[:REGISTERS]->(e4a),(s)-[:HAS_GRADE{grade:1}]->(e4a),
                  (s)-[:REGISTERS]->(e5a),(s)-[:HAS_GRADE{grade:3}]->(e5a)
                """);

        log.info("Database seeding complete. Nodes created: students=8, professors=9, lectures=8.");
    }
}
