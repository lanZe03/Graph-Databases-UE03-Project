package at.jku.studentgraphdb.service;

import at.jku.studentgraphdb.models.Notification;
import at.jku.studentgraphdb.models.Student;
import at.jku.studentgraphdb.repository.NotificationRepository;
import at.jku.studentgraphdb.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final NotificationRepository notificationRepository;

    public StudentService(StudentRepository studentRepository, NotificationRepository notificationRepository) {
        this.studentRepository = studentRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAllByOrderByNameAsc();
    }

    @Transactional
    public String deleteStudentByMatriculationNumber(String matriculationNumber, String reason) {
        Optional<Student> optionalStudent = studentRepository.findByMatriculationNumber(matriculationNumber);

        return optionalStudent.map(student -> deleteStudent(reason, student)).orElseGet(() -> "No student found with the matriculation number " + matriculationNumber);

    }

    @Transactional
    public String deleteStudentByName(String studentName, String reason) {
        Optional<Student> optionalStudent = studentRepository.findByName(studentName);

        return optionalStudent.map(student -> deleteStudent(reason, student)).orElseGet(() -> "No student found with the name " + studentName);
    }

    private String deleteStudent(String reason, Student student) {
        studentRepository.deleteStudentByMatriculationNumber(student.getMatriculationNumber());

        Notification notification = Notification.builder()
                .deletedEntityType("Student")
                .deletedEntityId(student.getMatriculationNumber())
                .message("Deleted Student " + student.getName() + " (" + student.getMatriculationNumber() + ")")
                .reason(reason)
                .deletedAt(OffsetDateTime.now().toString())
                .build();

        notificationRepository.save(notification);
        return "Student " + student.getName() + " deleted";
    }
}
