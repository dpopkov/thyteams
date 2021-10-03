package learn.thyme.thyteams.team;

import io.github.wimdeblauwe.jpearl.AbstractEntityId;

import java.util.UUID;

public class TeamPlayerId extends AbstractEntityId<UUID> {

    protected TeamPlayerId() {
    }

    public TeamPlayerId(UUID id) {
        super(id);
    }
}
