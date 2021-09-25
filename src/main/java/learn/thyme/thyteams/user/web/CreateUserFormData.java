package learn.thyme.thyteams.user.web;

import learn.thyme.thyteams.infrastructure.validation.ValidationGroupTwo;
import learn.thyme.thyteams.user.CreateUserParameters;
import learn.thyme.thyteams.user.PhoneNumber;
import learn.thyme.thyteams.user.UserName;

import javax.validation.constraints.*;

@NotExistingUser(groups = ValidationGroupTwo.class)
@PasswordsMatch(groups = ValidationGroupTwo.class)
public class CreateUserFormData extends AbstractUserFormData {
    @NotBlank
    private String password;
    @NotBlank
    private String passwordRepeated;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

    public CreateUserParameters toParameters() {
        return new CreateUserParameters(new UserName(getFirstName(), getLastName()),
                password,
                getGender(),
                getBirthday(),
                new learn.thyme.thyteams.user.Email(getEmail()),
                new PhoneNumber(getPhoneNumber()));
    }
}
