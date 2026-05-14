package at.jku.studentgraphdb.controller;

import at.jku.studentgraphdb.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/delete")
    public String showDeletePage(Model model) {
        model.addAttribute("students", studentService.findAllStudents());
        return "delete-student";
    }

    @PostMapping("/delete/by-matriculation")
    public String deleteByMatriculation(@RequestParam String matriculationNumber,
                                        @RequestParam String reason,
                                        Model model) {
        String result = studentService.deleteStudentByMatriculationNumber(matriculationNumber, reason);
        model.addAttribute("message", result);
        model.addAttribute("students", studentService.findAllStudents());
        return "delete-student";
    }

    @PostMapping("/delete/by-name")
    public String deleteByName(@RequestParam String name,
                               @RequestParam String reason,
                               Model model) {
        String result = studentService.deleteStudentByName(name, reason);
        model.addAttribute("message", result);
        model.addAttribute("students", studentService.findAllStudents());
        return "delete-student";
    }
}
