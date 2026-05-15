package at.jku.studentgraphdb.service;

import at.jku.studentgraphdb.dto.StudentExam;
import at.jku.studentgraphdb.models.Student;
import at.jku.studentgraphdb.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentGradingService {
    private final StudentRepository studentRepository;

    public StudentGradingService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentExam> findRegisteredExams(String matriculationNumber) {
        if (matriculationNumber == null || matriculationNumber.isBlank()) {
            return Collections.emptyList();
        }
        return studentRepository.findRegisteredExams(matriculationNumber);
    }

    @Transactional
    public String gradeStudent(String matriculationNumber,
                               String lectureId,
                               String examDate,
                               String examRoom,
                               Long grade) {

        if (grade == null || grade < 1 || grade > 5) {
            return "Grade must be between 1 and 5.";
        }

        Optional<Student> student = studentRepository.findByMatriculationNumber(matriculationNumber);
        if (student.isEmpty()) {
            return "Student not found: " + matriculationNumber;
        }

        boolean hasAnyRegisteredExamForLecture = studentRepository.isStudentRegisteredForLectureExam(matriculationNumber, lectureId);

        if (!hasAnyRegisteredExamForLecture) {
            return "Student is not registered for an exam of lecture " + lectureId + ".";
        }

        studentRepository.saveGrade(matriculationNumber, lectureId, examDate, examRoom, grade);
        return "Grade saved successfully. Student " + student.get() + " has grade " + grade + " in Lecture " + lectureId;
    }
}
