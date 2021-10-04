package learn.thyme.thyteams.team.web;

import learn.thyme.thyteams.team.CreateTeamParameters;
import learn.thyme.thyteams.team.TeamPlayerParameters;
import learn.thyme.thyteams.user.UserId;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateTeamFormData {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotNull
    private UserId coachId;

    @NotNull
    @Size(min = 1)
    @Valid
    private TeamPlayerFormData[] players;

    public CreateTeamFormData() {
        players = new TeamPlayerFormData[]{new TeamPlayerFormData()};
    }

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

    public TeamPlayerFormData[] getPlayers() {
        return players;
    }

    public void setPlayers(TeamPlayerFormData[] players) {
        this.players = players;
    }

    public CreateTeamParameters toParameters() {
        return new CreateTeamParameters(name, coachId, getTeamPlayerParameters());
    }

    protected Set<TeamPlayerParameters> getTeamPlayerParameters() {
        return Arrays.stream(players)
                .map(tpFormData -> new TeamPlayerParameters(tpFormData.getPlayerId(), tpFormData.getPosition()))
                .collect(Collectors.toSet());
    }

    public void removeEmptyTeamPlayerForms() {
        setPlayers(Arrays.stream(players)
                .filter(this::isNotEmptyTeamPlayerForm)
                .toArray(TeamPlayerFormData[]::new));
    }

    private boolean isNotEmptyTeamPlayerForm(TeamPlayerFormData formData) {
        return formData != null
                && formData.getPlayerId() != null
                && formData.getPosition() != null;
    }
}
