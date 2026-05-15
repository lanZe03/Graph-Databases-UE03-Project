package at.jku.studentgraphdb.service;

import at.jku.studentgraphdb.repository.ProfessorRepository;
import at.jku.studentgraphdb.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;

    public ConnectionService(StudentRepository studentRepository, ProfessorRepository professorRepository) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
    }

    public String findPeopleConnection(String nameA, String nameB) {
        if (nameA == null || nameA.isBlank() || nameB == null || nameB.isBlank()) {
            return "Please provide both names";
        }

        if (studentRepository.areClassmates(nameA, nameB)) {
            return nameA + " and " + nameB + " are classmates";
        }

        if (professorRepository.areColleagues(nameA, nameB)) {
            return nameA + " and " + nameB + " are colleagues";
        }

        return "No connection between " + nameA + " and " + nameB;
    }
}

