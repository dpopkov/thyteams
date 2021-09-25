package learn.thyme.thyteams.user.web;

import learn.thyme.thyteams.user.Email;
import learn.thyme.thyteams.user.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotExistingUserValidator implements ConstraintValidator<NotExistingUser, AbstractUserFormData> {
    private final UserService userService;

    public NotExistingUserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(NotExistingUser constraintAnnotation) {
        // intentionally empty
    }

    @Override
    public boolean isValid(AbstractUserFormData formData, ConstraintValidatorContext context) {
        if (!StringUtils.isEmpty(formData.getEmail())
                && userService.userWithEmailExists(new Email(formData.getEmail()))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{UserAlreadyExisting}")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
