package at.jku.studentgraphdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotificationController {
    @GetMapping("/notifications")
    // this gets the notification from the GlobalModelAttributes
    public String notifications() {
        return "notifications";
    }
}
