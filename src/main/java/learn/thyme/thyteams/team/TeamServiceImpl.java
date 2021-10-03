package learn.thyme.thyteams.team;

import learn.thyme.thyteams.user.User;
import learn.thyme.thyteams.user.UserId;
import learn.thyme.thyteams.user.UserNotFoundException;
import learn.thyme.thyteams.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository repository;
    private final UserService userService;

    public TeamServiceImpl(TeamRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamSummary> getTeams(Pageable pageable) {
        return repository.findAllSummary(pageable);
    }

    @Override
    public Team createTeam(CreateTeamParameters parameters) {
        final String name = parameters.getName();
        final User coach = getUser(parameters.getCoachId());
        log.info("Creating team {} with coach {} ({})", name, coach.getUserName().getFullName(), coach.getId());
        Team team = new Team(repository.nextId(), name, coach);
        final Set<TeamPlayerParameters> players = parameters.getPlayers();
        for (TeamPlayerParameters tpParams : players) {
            TeamPlayerId tpId = repository.nextPlayerId();
            User player = getUser(tpParams.getPlayerId());
            team.addPlayer(new TeamPlayer(tpId, player, tpParams.getPosition()));
        }
        return repository.save(team);
    }

    @Override
    public Optional<Team> getTeam(TeamId teamId) {
        return repository.findById(teamId);
    }

    @Override
    public Optional<Team> getTeamWithPlayers(TeamId teamId) {
        return repository.findTeamWithPlayers(teamId);
    }

    @Override
    public Team editTeam(TeamId teamId, EditTeamParameters parameters) {
        Team team = getTeam(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        if (team.getVersion() != parameters.getVersion()) {
            throw new ObjectOptimisticLockingFailureException(Team.class, team.getId().asString());
        }
        team.setName(parameters.getName());
        team.setCoach(getUser(parameters.getCoachId())); // Saving the updated team is done automatically by JPA/Hibernate.
        team.setPlayers(parameters.getPlayers().stream()
                .map(tpParams -> new TeamPlayer(
                        repository.nextPlayerId(),
                        getUser(tpParams.getPlayerId()),
                        tpParams.getPosition())
                ).collect(Collectors.toSet()));
        return team;
    }

    @Override
    public void deleteTeam(TeamId teamId) {
        repository.deleteById(teamId);
    }

    @Override
    public void deleteAllTeams() {
        repository.deleteAll();
    }

    @Override
    public Team addPlayer(TeamId teamId, long version, UserId userId, PlayerPosition position) {
        Team team = getTeam(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        if (team.getVersion() != version) {
            throw new ObjectOptimisticLockingFailureException(User.class, team.getId().asString());
        }
        team.addPlayer(new TeamPlayer(repository.nextPlayerId(),
                        getUser(userId),
                        position));
        return team;
    }

    private User getUser(UserId userId) {
        return userService.getUser(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
