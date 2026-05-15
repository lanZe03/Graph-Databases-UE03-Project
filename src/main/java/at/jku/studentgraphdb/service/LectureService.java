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

    public List<Lecture> searchLecture(String searchString) {
        // if we have no search string,
        // we return all lectures, so the display is nice
        if (searchString == null || searchString.isBlank()) {
            return lectureRepository.findAllByOrderByTopicAsc();
        }
        return lectureRepository.searchLectures(searchString);
    }

}
