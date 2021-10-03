package learn.thyme.thyteams.team.web;

import learn.thyme.thyteams.team.PlayerPosition;
import learn.thyme.thyteams.team.TeamPlayer;
import learn.thyme.thyteams.user.UserId;

import javax.validation.constraints.NotNull;

/**
 * Contains information of a single player and his position on the team.
 */
public class TeamPlayerFormData {
    @NotNull
    private UserId playerId;
    @NotNull
    private PlayerPosition position;

    public UserId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UserId playerId) {
        this.playerId = playerId;
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    public static TeamPlayerFormData from(TeamPlayer teamPlayer) {
        TeamPlayerFormData result = new TeamPlayerFormData();
        result.setPlayerId(teamPlayer.getPlayer().getId());
        result.setPosition(teamPlayer.getPosition());
        return result;
    }
}
