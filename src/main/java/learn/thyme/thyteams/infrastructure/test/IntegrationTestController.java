package learn.thyme.thyteams.infrastructure.test;

import learn.thyme.thyteams.team.TeamService;
import learn.thyme.thyteams.user.*;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/integration-test")
@Profile("integration-test")
public class IntegrationTestController {

    private final UserService userService;
    private final TeamService teamService;

    public IntegrationTestController(UserService userService, TeamService teamService) {
        this.userService = userService;
        this.teamService = teamService;
    }

    @PostMapping("/reset-db")
    public void resetDatabase() {
        teamService.deleteAllTeams();
        userService.deleteAllUsers();
        addUser();
        addAdministrator();
    }

    @PostMapping("/add-test-team")
    public void addTestTeam() {
        UserNameAndId userNameAndId = userService.getAllUsersNameAndId().first();
        teamService.createTeam("Test Team", userNameAndId.getId());
    }

    private void addUser() {
        userService.createUser(new CreateUserParameters(new UserName("User", "Last"),
                "user-pwd",
                Gender.MALE,
                LocalDate.parse("2001-04-12"),
                new Email("user.last@mail.org"),
                new PhoneNumber("+567 789 432")));
    }

    private void addAdministrator() {
        userService.createAdministrator(new CreateUserParameters(new UserName("Admin", "First"),
                "admin-pwd",
                Gender.FEMALE,
                LocalDate.parse("2001-04-17"),
                new Email("admin.first@mail.org"),
                new PhoneNumber("+567 789 321")));
    }
}
