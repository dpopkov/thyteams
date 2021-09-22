package learn.thyme.thyteams.user.web;

import learn.thyme.thyteams.infrastructure.validation.ValidationGroupOne;
import learn.thyme.thyteams.infrastructure.validation.ValidationGroupTwo;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, ValidationGroupOne.class, ValidationGroupTwo.class})
public interface CreateUserValidationGroupSequence {
}
