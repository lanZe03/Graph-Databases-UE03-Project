package at.jku.studentgraphdb.config;

import at.jku.studentgraphdb.service.LectureService;
import at.jku.studentgraphdb.service.StudentService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    private final LectureService lectureService;
    private final StudentService studentService;

    public GlobalModelAttributes(LectureService lectureService, StudentService studentService) {
        this.lectureService = lectureService;
        this.studentService = studentService;
    }

    @ModelAttribute("allLectures")
    public Object addLecturesToModel() {
        return lectureService.findAllLectures();
    }

    @ModelAttribute("allStudents")
    public Object addStudentsToModel() {
        return studentService.findAllStudents();
    }

}
