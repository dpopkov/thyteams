package learn.thyme.thyteams.infrastructure.test;

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

    public IntegrationTestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reset-db")
    public void resetDatabase() {
        userService.deleteAllUsers();
        addUser();
        addAdministrator();
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
