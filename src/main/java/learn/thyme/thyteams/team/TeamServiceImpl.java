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
    public Team createTeam(String name, User coach) {
        log.info("Creating team {} with coach {} ({})", name, coach.getUserName().getFullName(), coach.getId());
        return repository.save(new Team(repository.nextId(), name, coach));
    }

    @Override
    public Team createTeam(String name, UserId coachId) {
        User coach = getCoach(coachId);
        return createTeam(name, coach);
    }

    @Override
    public Optional<Team> getTeam(TeamId teamId) {
        return repository.findById(teamId);
    }

    @Override
    public Team editTeam(TeamId teamId, long version, String name, UserId coachId) {
        Team team = getTeam(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        if (team.getVersion() != version) {
            throw new ObjectOptimisticLockingFailureException(Team.class, team.getId().asString());
        }
        team.setName(name);
        team.setCoach(getCoach(coachId)); // Saving the updated team is done automatically by JPA/Hibernate.
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

    private User getCoach(UserId coachId) {
        return userService.getUser(coachId)
                .orElseThrow(() -> new UserNotFoundException(coachId));
    }
}
