package learn.thyme.thyteams.user;

public class UserServiceException extends RuntimeException {
    public UserServiceException(Exception e) {
        super(e);
    }
}
