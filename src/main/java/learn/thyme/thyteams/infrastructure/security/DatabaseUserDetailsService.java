package learn.thyme.thyteams.infrastructure.security;

import learn.thyme.thyteams.user.Email;
import learn.thyme.thyteams.user.User;
import learn.thyme.thyteams.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // The parameter 'username' represents actually an email, since we are searching a user by email
        User user = userRepository.findByEmail(new Email(username))
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with email %s could not be found", username)));
        return new ApplicationUserDetails(user);
    }
}
