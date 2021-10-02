package learn.thyme.thyteams.team.web;

import learn.thyme.thyteams.team.TeamId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringToTeamIdConverter implements Converter<String, TeamId> {
    @Override
    public TeamId convert(@NonNull String s) {
        return new TeamId(UUID.fromString(s));
    }
}
