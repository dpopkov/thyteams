package learn.thyme.thyteams.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User createUser(CreateUserParameters parameters);

    Page<User> getUsers(Pageable pageable);

    boolean userWithEmailExists(Email email);
}
