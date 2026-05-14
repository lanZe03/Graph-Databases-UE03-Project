package at.jku.studentgraphdb.controller;

import at.jku.studentgraphdb.models.Lecture;
import at.jku.studentgraphdb.service.LectureParticipantService;
import at.jku.studentgraphdb.service.LectureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/lectures")
public class LectureParticipantController {

    private final LectureParticipantService lectureParticipantService;
    private final LectureService lectureService;

    public LectureParticipantController(LectureParticipantService lectureParticipantService, LectureService lectureService) {
        this.lectureParticipantService = lectureParticipantService;
        this.lectureService = lectureService;
    }

    @GetMapping("/participants")
    public String showPage(@RequestParam(required = false) String lectureId, Model model) {
        if (lectureId != null && !lectureId.isBlank()) {
            Optional<Lecture> lecture = lectureService.findLecture(lectureId);
            if (lecture.isPresent()) {
                model.addAttribute("lecture", lecture.get());
                model.addAttribute("participants", lectureParticipantService.findParticipants(lectureId));
                model.addAttribute("selectedLectureId", lectureId);
            } else {
                model.addAttribute("message", "Lecture not found: " + lectureId);
            }
        }

        return "lecture-participants";
    }

    @PostMapping("/participants/search")
    public String searchLecture(@RequestParam String lectureId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("lectureId", lectureId);
        return "redirect:/lectures/participants";
    }

    @PostMapping("/participants/add")
    public String addParticipant(@RequestParam String lectureId,
                                 @RequestParam String matriculationNumber,
                                 RedirectAttributes redirectAttributes) {
        String result = lectureParticipantService.addParticipant(lectureId, matriculationNumber);
        redirectAttributes.addFlashAttribute("message", result);
        redirectAttributes.addAttribute("lectureId", lectureId);
        return "redirect:/lectures/participants";
    }

    @PostMapping("/participants/remove")
    public String removeParticipant(@RequestParam String lectureId,
                                    @RequestParam String matriculationNumber,
                                    RedirectAttributes redirectAttributes) {
        String result = lectureParticipantService.removeParticipant(lectureId, matriculationNumber);
        redirectAttributes.addFlashAttribute("message", result);
        redirectAttributes.addAttribute("lectureId", lectureId);
        return "redirect:/lectures/participants";
    }

    @PostMapping("/create")
    public String createLecture(@RequestParam String lectureId,
                                @RequestParam String topic,
                                @RequestParam(required = false) Integer ects,
                                RedirectAttributes redirectAttributes) {
        String result = lectureParticipantService.createLecture(lectureId, topic, ects);
        redirectAttributes.addFlashAttribute("message", result);
        redirectAttributes.addAttribute("lectureId", lectureId);
        return "redirect:/lectures/participants";
    }
}