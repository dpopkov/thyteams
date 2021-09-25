package learn.thyme.thyteams.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    User createUser(CreateUserParameters parameters);

    User createAdministrator(CreateUserParameters parameters);

    Optional<User> getUser(UserId userId);

    User editUser(UserId userId, EditUserParameters parameters);

    Page<User> getUsers(Pageable pageable);

    boolean userWithEmailExists(Email email);

    void deleteUser(UserId userId);
}
