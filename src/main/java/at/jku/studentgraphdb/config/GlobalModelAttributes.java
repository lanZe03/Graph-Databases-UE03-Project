package at.jku.studentgraphdb.config;

import at.jku.studentgraphdb.service.LectureService;
import at.jku.studentgraphdb.service.NotificationService;
import at.jku.studentgraphdb.service.StudentService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    private final LectureService lectureService;
    private final StudentService studentService;
    private final NotificationService notificationService;

    public GlobalModelAttributes(LectureService lectureService, StudentService studentService, NotificationService notificationService) {
        this.lectureService = lectureService;
        this.studentService = studentService;
        this.notificationService = notificationService;
    }

    @ModelAttribute("allLectures")
    public Object addLecturesToModel() {
        return lectureService.findAllLectures();
    }

    @ModelAttribute("allStudents")
    public Object addStudentsToModel() {
        return studentService.findAllStudents();
    }

    @ModelAttribute("allNotifications")
    public Object addNotificationsToModel() {
        return notificationService.findAllNotifications();
    }

}
