package at.jku.studentgraphdb.controller;


import at.jku.studentgraphdb.service.LectureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/lectures")
@Controller
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/search")
    public String searchLectures(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("searchText", q);
        model.addAttribute("lectures", lectureService.searchLecture(q));
        return "lecture-search";
    }

}
