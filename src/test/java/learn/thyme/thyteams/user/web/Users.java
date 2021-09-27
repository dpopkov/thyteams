package learn.thyme.thyteams.user.web;

import learn.thyme.thyteams.user.*;

import java.time.LocalDate;
import java.util.UUID;

public class Users {

    public static User createUser(UserName userName, String birthDay) {
        return User.createUser(new UserId(UUID.randomUUID()),
                userName,
                "fake-encoded-password",
                Gender.MALE,
                LocalDate.parse(birthDay),
                new Email(String.format("%s.%s@mail.org",
                        userName.getFirstName().toLowerCase(), userName.getLastName().toLowerCase())),
                new PhoneNumber("+567 123 456"));
    }
}
