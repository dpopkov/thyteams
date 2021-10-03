package learn.thyme.thyteams.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface TeamRepository extends CrudRepository<Team, TeamId>, TeamRepositoryCustom {
    @Query("SELECT new learn.thyme.thyteams.team.TeamSummary(t.id, t.name, t.coach.id, t.coach.userName) FROM Team t")
    Page<TeamSummary> findAllSummary(Pageable pageable);

    // Use JOIN FETCH to retrieve the team with the linked players in a single SQL statement.
    @Query("FROM Team t JOIN FETCH t.players WHERE t.id = :id")
    Optional<Team> findTeamWithPlayers(@Param("id") TeamId id);
}
