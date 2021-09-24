package learn.thyme.thyteams.infrastructure.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")   // The POST to /login is handle by Spring Security
    public String login(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) { // if there is no logged on user
            return "login";
        } else {
            return "redirect:/";    // avoid showing the login page to an already logged on user
        }
    }
}
