package at.jku.studentgraphdb.service;

import at.jku.studentgraphdb.models.Lecture;
import at.jku.studentgraphdb.repository.LectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LectureService {
    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public List<Lecture> findAllLectures() {
        return lectureRepository.findAllByOrderByTopicAsc();
    }

    public Optional<Lecture> findLecture(String lectureId) {
        return lectureRepository.findById(lectureId);
    }

}
