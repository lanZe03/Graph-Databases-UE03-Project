package at.jku.studentgraphdb.controller;

import at.jku.studentgraphdb.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/delete")
    public String showDeletePage() {
        return "delete-student";
    }

    @PostMapping("/delete/by-matriculation")
    public String deleteByMatriculation(@RequestParam String matriculationNumber,
                                        @RequestParam String reason,
                                        RedirectAttributes redirectAttributes) {
        String result = studentService.deleteStudentByMatriculationNumber(matriculationNumber, reason);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/students/delete";
    }

    @PostMapping("/delete/by-name")
    public String deleteByName(@RequestParam String name,
                               @RequestParam String reason,
                               RedirectAttributes redirectAttributes) {
        String result = studentService.deleteStudentByName(name, reason);
        redirectAttributes.addFlashAttribute("message", result);
        return "redirect:/students/delete";
    }
}
