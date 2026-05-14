package at.jku.studentgraphdb.service;

import at.jku.studentgraphdb.models.Lecture;
import at.jku.studentgraphdb.models.Student;
import at.jku.studentgraphdb.repository.LectureRepository;
import at.jku.studentgraphdb.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LectureParticipantService {
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;

    public LectureParticipantService(LectureRepository lectureRepository, StudentRepository studentRepository) {
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
    }

    public List<Student> findParticipants(String lectureId) {
        return studentRepository.findParticipantsByLectureId(lectureId);
    }

    @Transactional
    public String addParticipant(String lectureId, String matriculationId) {
        Optional<Lecture> lecture = lectureRepository.findById(lectureId);
        if (lecture.isEmpty()) {
            return "Lecture not found: " + lectureId;
        }

        Optional<Student> student = studentRepository.findByMatriculationNumber(matriculationId);
        if (student.isEmpty()) {
            return "Student not found: " + matriculationId;
        }

        studentRepository.addStudentToLecture(matriculationId, lectureId);
        return "Student " + student.get() + " added to lecture " + lecture.get() + " successfully.";
    }

    @Transactional
    public String removeParticipant(String lectureId, String matriculationNumber) {
        Optional<Lecture> lecture = lectureRepository.findById(lectureId);
        if (lecture.isEmpty()) {
            return "Lecture not found: " + lectureId;
        }

        Optional<Student> student = studentRepository.findByMatriculationNumber(matriculationNumber);
        if (student.isEmpty()) {
            return "Student not found: " + matriculationNumber;
        }

        studentRepository.removeStudentFromLecture(matriculationNumber, lectureId);
        return "Student " + student.get() + " removed from lecture " + lecture.get() + " successfully.";
    }

    @Transactional
    public String createLecture(String lectureId, String topic, Integer ects) {
        if (lectureRepository.existsById(lectureId)) {
            return "Lecture already exists: " + lectureId;
        }

        Lecture lecture = Lecture.builder()
                .id(lectureId)
                .topic(topic)
                .ects(ects)
                .build();

        lectureRepository.save(lecture);
        return "Lecture " + lecture + " created successfully.";
    }
}
