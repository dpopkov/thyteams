package learn.thyme.thyteams.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(CreateUserParameters parameters) {
        log.debug("Creating user {} ({})", parameters.getUserName().getFullName(),
                parameters.getEmail().asString());
        UserId userId = repository.nextId();
        User user = User.createUser(userId,
                parameters.getUserName(),
                passwordEncoder.encode(parameters.getPassword()),
                parameters.getGender(),
                parameters.getBirthday(),
                parameters.getEmail(),
                parameters.getPhoneNumber());
        storeAvatarIfPresent(parameters, user);
        return repository.save(user);
    }

    @Override
    public User createAdministrator(CreateUserParameters parameters) {
        log.debug("Creating administrator {} ({})", parameters.getUserName().getFullName(),
                parameters.getEmail().asString());
        UserId userId = repository.nextId();
        User user = User.createAdministrator(userId,
                parameters.getUserName(),
                passwordEncoder.encode(parameters.getPassword()),
                parameters.getGender(),
                parameters.getBirthday(),
                parameters.getEmail(),
                parameters.getPhoneNumber());
        storeAvatarIfPresent(parameters, user);
        return repository.save(user);
    }

    private void storeAvatarIfPresent(CreateUserParameters parameters, User user) {
        MultipartFile avatar = parameters.getAvatar();
        if (avatar != null) {
            try {
                user.setAvatar(avatar.getBytes());
            } catch (IOException e) {
                throw new UserServiceException(e);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUser(UserId userId) {
        return repository.findById(userId);
    }

    @Override
    public User editUser(UserId userId, EditUserParameters parameters) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (parameters.getVersion() != user.getVersion()) {
            throw new ObjectOptimisticLockingFailureException(User.class, user.getId().asString());
        }
        parameters.update(user); // Saving the updated user is done automatically by JPA/Hibernate.
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getUsers(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userWithEmailExists(Email email) {
        return repository.existsByEmail(email);
    }

    @Override
    public void deleteUser(UserId userId) {
        repository.deleteById(userId);
    }

    @Override
    public void deleteAllUsers() {
        repository.deleteAll();
    }

    @Override
    public long countUsers() {
        return repository.count();
    }
}
