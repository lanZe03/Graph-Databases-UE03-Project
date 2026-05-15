package at.jku.studentgraphdb.controller;

import at.jku.studentgraphdb.service.PathService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/connections")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public String showPathPage(@RequestParam(required = false) String fromValue,
                               @RequestParam(required = false) String toValue,
                               Model model) {

        model.addAttribute("fromValue", fromValue);
        model.addAttribute("toValue", toValue);

        if (fromValue != null && !fromValue.isBlank() && toValue != null && !toValue.isBlank()) {
            model.addAttribute("paths", pathService.findPaths(fromValue, toValue));
        }

        return "path-search";
    }
}