package learn.thyme.thyteams.user;

import io.github.wimdeblauwe.jpearl.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tt_user")
public class User extends AbstractEntity<UserId> {

    /**
     * Default constructor for JPA (application code should never use that constructor directly)
     */
    protected User() {
    }

    public User(UserId id) {
        super(id);
    }
}
