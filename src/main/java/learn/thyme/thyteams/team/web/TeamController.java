package learn.thyme.thyteams.team.web;

import learn.thyme.thyteams.infrastructure.web.EditMode;
import learn.thyme.thyteams.team.Team;
import learn.thyme.thyteams.team.TeamId;
import learn.thyme.thyteams.team.TeamNotFoundException;
import learn.thyme.thyteams.team.TeamService;
import learn.thyme.thyteams.user.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamService service;
    private final UserService userService;

    public TeamController(TeamService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model,
                        @SortDefault.SortDefaults(@SortDefault("name"))Pageable pageable) {
        model.addAttribute("teams", service.getTeams(pageable));
        return "teams/list";
    }

    @GetMapping("/create")
    @Secured("ROLE_ADMIN")
    public String createTeamForm(Model model) {
        model.addAttribute("team", new CreateTeamFormData());
        model.addAttribute("users", userService.getAllUsersNameAndId());
        return "teams/edit";
    }

    @PostMapping("/create")
    @Secured("ROLE_ADMIN")
    public String doCreateTeam(@Valid @ModelAttribute("team") CreateTeamFormData formData,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.CREATE);
            model.addAttribute("users", userService.getAllUsersNameAndId());
            return "teams/edit";
        }
        service.createTeam(formData.getName(), formData.getCoachId());
        return "redirect:/teams";
    }

    @GetMapping("/{id}")
    public String editTeamForm(@PathVariable("id") TeamId teamId,
                               Model model) {
        Team team = service.getTeam(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        model.addAttribute("team", EditTeamFormData.from(team));
        model.addAttribute("users", userService.getAllUsersNameAndId());
        model.addAttribute("editMode", EditMode.UPDATE);
        return "teams/edit";
    }

    @PostMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public String doEditTeam(@PathVariable("id") TeamId teamId,
                             @Valid @ModelAttribute("team") EditTeamFormData formData,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            model.addAttribute("users", userService.getAllUsersNameAndId());
            return "teams/edit";
        }
        service.editTeam(teamId, formData.getVersion(), formData.getName(), formData.getCoachId());
        return "redirect:/teams";
    }

    @PostMapping("/{id}/delete")
    @Secured("ROLE_ADMIN")
    public String doDeleteTeam(@PathVariable("id") TeamId teamId,
                               RedirectAttributes redirectAttributes) {
        Team team = service.getTeam(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        service.deleteTeam(teamId);
        redirectAttributes.addFlashAttribute("deletedTeamName", team.getName());
        return "redirect:/teams";
    }
}
