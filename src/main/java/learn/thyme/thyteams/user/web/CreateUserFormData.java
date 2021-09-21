package learn.thyme.thyteams.user.web;

import learn.thyme.thyteams.infrastructure.validation.ValidationGroupOne;
import learn.thyme.thyteams.infrastructure.validation.ValidationGroupTwo;
import learn.thyme.thyteams.user.CreateUserParameters;
import learn.thyme.thyteams.user.Gender;
import learn.thyme.thyteams.user.PhoneNumber;
import learn.thyme.thyteams.user.UserName;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@NotExistingUser(groups = ValidationGroupTwo.class)
public class CreateUserFormData {
    @NotBlank
    @Size(max = 32, groups = ValidationGroupOne.class)
    private String firstName;
    @NotBlank
    @Size(max = 32, groups = ValidationGroupOne.class)
    private String lastName;
    @NotNull
    private Gender gender;
    @NotBlank
    @Email(groups = ValidationGroupOne.class)
    private String email;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @NotBlank
    @Pattern(regexp = "[0-9.\\-() x/+]+", groups = ValidationGroupOne.class)
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public CreateUserParameters toParameters() {
        return new CreateUserParameters(new UserName(firstName, lastName),
                gender,
                birthday,
                new learn.thyme.thyteams.user.Email(email),
                new PhoneNumber(phoneNumber));
    }
}