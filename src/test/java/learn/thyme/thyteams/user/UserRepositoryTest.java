package learn.thyme.thyteams.user;

import io.github.wimdeblauwe.jpearl.InMemoryUniqueIdGenerator;
import io.github.wimdeblauwe.jpearl.UniqueIdGenerator;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("data-jpa-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    private final UserRepository repository;
    private final JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UserRepositoryTest(UserRepository repository,
                           JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void validatePreconditions() {
        assertThat(repository.count()).isZero();
    }

    @Test
    void testSaveUser() {
        UserId id = repository.nextId();
        repository.save(new User(id,
                new UserName("Jack", "Sparrow"),
                Gender.MALE,
                LocalDate.of(1999, Month.APRIL, 17),
                new Email("jack@mail.org"),
                new PhoneNumber("201 432 0128")));

        entityManager.flush();

        UUID idInDb = jdbcTemplate.queryForObject("SELECT id FROM tt_user", UUID.class);
        assertThat(idInDb).isEqualTo(id.getId());
        assertThat(jdbcTemplate.queryForObject("SELECT first_name FROM tt_user", String.class)).isEqualTo("Jack");
        assertThat(jdbcTemplate.queryForObject("SELECT  last_name FROM tt_user", String.class)).isEqualTo("Sparrow");
        assertThat(jdbcTemplate.queryForObject("SELECT gender FROM tt_user", Gender.class)).isEqualTo(Gender.MALE);
        assertThat(jdbcTemplate.queryForObject("SELECT birthday FROM tt_user", LocalDate.class)).isEqualTo("1999-04-17");
        assertThat(jdbcTemplate.queryForObject("SELECT email FROM tt_user", String.class)).isEqualTo("jack@mail.org");
        assertThat(jdbcTemplate.queryForObject("SELECT phone_number FROM tt_user", String.class)).isEqualTo("201 432 0128");
    }

    @org.springframework.boot.test.context.TestConfiguration
    static class TestConfiguration {
        @Bean
        public UniqueIdGenerator<UUID> uniqueIdGenerator() {
            return new InMemoryUniqueIdGenerator();
        }
    }
}
