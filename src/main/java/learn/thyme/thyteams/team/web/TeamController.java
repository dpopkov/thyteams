package learn.thyme.thyteams.team.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teams")
public class TeamController {
    @GetMapping
    public String index() {
        return "teams/list";
    }
}
