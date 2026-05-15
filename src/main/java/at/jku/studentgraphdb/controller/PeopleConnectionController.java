package at.jku.studentgraphdb.controller;

import at.jku.studentgraphdb.service.ConnectionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/connections")
public class PeopleConnectionController {

    private final ConnectionService connectionService;

    public PeopleConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping("/people")
    public String showPeopleConnectionPage(@RequestParam(required = false) String nameA,
                                           @RequestParam(required = false) String nameB,
                                           Model model) {
        model.addAttribute("nameA", nameA);
        model.addAttribute("nameB", nameB);

        if (nameA != null && !nameA.isBlank() && nameB != null && !nameB.isBlank()) {
            model.addAttribute("result", connectionService.findPeopleConnection(nameA, nameB));
        }

        return "people-connection";
    }
}