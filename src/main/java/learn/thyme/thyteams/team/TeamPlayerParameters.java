package learn.thyme.thyteams.team;

import learn.thyme.thyteams.user.UserId;

public class TeamPlayerParameters {
    private final UserId playerId;
    private final PlayerPosition position;

    public TeamPlayerParameters(UserId playerId, PlayerPosition position) {
        this.playerId = playerId;
        this.position = position;
    }

    public UserId getPlayerId() {
        return playerId;
    }

    public PlayerPosition getPosition() {
        return position;
    }
}
