package learn.thyme.thyteams.user;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.time.LocalDate;

public class CreateUserParameters {
    private final UserName userName;
    /** Password in clear text. */
    private final String password;
    private final Gender gender;
    private final LocalDate birthday;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private MultipartFile avatar;

    public CreateUserParameters(UserName userName, String password, Gender gender, LocalDate birthday,
                                Email email, PhoneNumber phoneNumber) {
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public UserName getUserName() {
        return userName;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Email getEmail() {
        return email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    @Nullable
    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatarFile) {
        this.avatar = avatarFile;
    }
}
