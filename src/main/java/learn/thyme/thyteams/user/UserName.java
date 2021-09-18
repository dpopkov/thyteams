package learn.thyme.thyteams.user;

import com.google.common.base.MoreObjects;
import org.springframework.util.Assert;

import javax.persistence.Embeddable;

@Embeddable
public class UserName {
    private String firstName;
    private String lastName;

    protected UserName() {
    }

    public UserName(String firstName, String lastName) {
        Assert.hasText(firstName, "firstName cannot be blank");
        Assert.hasText(lastName, "lastName cannot be blank");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserName userName = (UserName) o;
        return firstName.equals(userName.firstName)
                && lastName.equals(userName.lastName);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .toString();
    }
}
