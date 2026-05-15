package at.jku.studentgraphdb.controller;

import at.jku.studentgraphdb.service.StudentGradingService;
import at.jku.studentgraphdb.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/exams")
public class StudentGradingController {

    private final StudentGradingService studentGradingService;
    private final StudentService studentService;

    public StudentGradingController(StudentGradingService studentGradingService, StudentService studentService) {
        this.studentGradingService = studentGradingService;
        this.studentService = studentService;
    }

    @GetMapping("/grade")
    public String showGradePage(@RequestParam(required = false) String studentSearch,
                                @RequestParam(required = false) String matriculationNumber,
                                Model model) {

        model.addAttribute("studentSearch", studentSearch);
        model.addAttribute("students", studentService.searchStudents(studentSearch));

        if (matriculationNumber != null && !matriculationNumber.isBlank()) {
            studentService.findStudentById(matriculationNumber).ifPresent(student -> {
                model.addAttribute("selectedStudent", student);
                model.addAttribute("registeredExams",
                        studentGradingService.findRegisteredExams(matriculationNumber));
            });
            model.addAttribute("selectedMatriculationNumber", matriculationNumber);
        }

        return "grade-student";
    }

    @PostMapping("/grade/save")
    public String saveGrade(@RequestParam String matriculationNumber,
                            @RequestParam String lectureId,
                            @RequestParam(required = false) String examDate,
                            @RequestParam(required = false) String examRoom,
                            @RequestParam Long grade,
                            RedirectAttributes redirectAttributes) {

        String result = studentGradingService.gradeStudent(
                matriculationNumber, lectureId, examDate, examRoom, grade
        );

        redirectAttributes.addFlashAttribute("message", result);
        redirectAttributes.addAttribute("matriculationNumber", matriculationNumber);
        return "redirect:/exams/grade";
    }
}
