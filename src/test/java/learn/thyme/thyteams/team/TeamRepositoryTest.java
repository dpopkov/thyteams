package learn.thyme.thyteams.team;

import io.github.wimdeblauwe.jpearl.InMemoryUniqueIdGenerator;
import io.github.wimdeblauwe.jpearl.UniqueIdGenerator;
import learn.thyme.thyteams.user.User;
import learn.thyme.thyteams.user.UserName;
import learn.thyme.thyteams.user.UserRepository;
import learn.thyme.thyteams.user.web.Users;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@ActiveProfiles("data-jpa-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeamRepositoryTest {
    private final TeamRepository repository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    TeamRepositoryTest(TeamRepository repository,
                       UserRepository userRepository,
                           JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void validatePreconditions() {
        assertThat(repository.count()).isZero();
    }

    @Test
    void testSaveTeam() {
        User coach = userRepository.save(Users.createUser(new UserName("Harlan", "Rees")));
        TeamId id = repository.nextId();
        repository.save(new Team(id, "Initiates", coach));
        entityManager.flush();

        assertThat(jdbcTemplate.queryForObject("SELECT id FROM team", UUID.class)).isEqualTo(id.getId());
        assertThat(jdbcTemplate.queryForObject("SELECT name FROM team", String.class)).isEqualTo("Initiates");
        assertThat(jdbcTemplate.queryForObject("SELECT coach_id FROM team", UUID.class)).isEqualTo(coach.getId().getId());
    }

    @Test
    void testFindAllSummary() {
        User coach = userRepository.save(Users.createUser(new UserName("Harlan", "Rees")));
        TeamId id = repository.nextId();
        repository.save(new Team(id, "Initiates", coach));

        Page<TeamSummary> teams = repository.findAllSummary(PageRequest.of(0, 10));
        assertThat(teams).hasSize(1)
                .extracting(TeamSummary::getId,
                        TeamSummary::getName,
                        TeamSummary::getCoachId,
                        TeamSummary::getCoachName)
                .contains(tuple(id,
                        "Initiates",
                        coach.getId(),
                        coach.getUserName()));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public UniqueIdGenerator<UUID> uniqueIdGenerator() {
            return new InMemoryUniqueIdGenerator();
        }
    }
}