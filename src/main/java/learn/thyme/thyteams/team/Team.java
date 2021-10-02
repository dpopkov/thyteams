package learn.thyme.thyteams.team;

import io.github.wimdeblauwe.jpearl.AbstractVersionedEntity;
import learn.thyme.thyteams.user.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Team extends AbstractVersionedEntity<TeamId> {

    @NotBlank
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User coach;

    protected Team() {
    }

    public Team(TeamId id, String name, User coach) {
        super(id);
        this.name = name;
        this.coach = coach;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }
}
