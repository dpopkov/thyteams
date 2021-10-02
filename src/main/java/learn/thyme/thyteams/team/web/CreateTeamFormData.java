package learn.thyme.thyteams.team.web;

import learn.thyme.thyteams.user.UserId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateTeamFormData {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotNull
    private UserId coachId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserId getCoachId() {
        return coachId;
    }

    public void setCoachId(UserId coachId) {
        this.coachId = coachId;
    }
}
