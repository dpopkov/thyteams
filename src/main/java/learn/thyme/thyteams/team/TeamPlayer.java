package learn.thyme.thyteams.team;

import io.github.wimdeblauwe.jpearl.AbstractEntity;
import learn.thyme.thyteams.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class TeamPlayer extends AbstractEntity<TeamPlayerId> {
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Team team;

    @OneToOne
    @NotNull
    private User player;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PlayerPosition position;

    protected TeamPlayer() {
    }

    public TeamPlayer(TeamPlayerId id, @NotNull User player, @NotNull PlayerPosition position) {
        super(id);
        this.player = player;
        this.position = position;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getPlayer() {
        return player;
    }

    public PlayerPosition getPosition() {
        return position;
    }
}
