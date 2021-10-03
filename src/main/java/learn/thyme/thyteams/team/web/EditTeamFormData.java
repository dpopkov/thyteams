package learn.thyme.thyteams.team.web;

import learn.thyme.thyteams.team.EditTeamParameters;
import learn.thyme.thyteams.team.Team;

public class EditTeamFormData extends CreateTeamFormData {
    private String id;
    private long version;

    public static EditTeamFormData from(Team team) {
        EditTeamFormData formData = new EditTeamFormData();
        formData.setId(team.getId().asString());
        formData.setVersion(team.getVersion());
        formData.setName(team.getName());
        formData.setCoachId(team.getCoach().getId());
        formData.setPlayers(team.getPlayers().stream()
                            .map(TeamPlayerFormData::from)
                            .toArray(TeamPlayerFormData[]::new));
        return formData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public EditTeamParameters toParameters() {
        return new EditTeamParameters(version,
                getName(),
                getCoachId(),
                getTeamPlayerParameters());
    }
}
