package learn.thyme.thyteams.infrastructure.security;

import learn.thyme.thyteams.user.User;
import learn.thyme.thyteams.user.UserId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains information of the user from the database.
 */
public class ApplicationUserDetails implements UserDetails {
    private final UserId id;
    private final String username;
    private final String displayName;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    public ApplicationUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getEmail().asString(); // using the email address as username
        this.displayName = user.getUserName().getFullName();
        this.password = user.getPassword(); // encrypted password
        this.authorities = user.getRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.name()))
                .collect(Collectors.toSet()); // map UserRole enum to GrantedAuthority instances
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserId getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }
}
