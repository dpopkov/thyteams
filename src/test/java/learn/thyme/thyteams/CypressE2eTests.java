package learn.thyme.thyteams;

import io.github.wimdeblauwe.testcontainers.cypress.CypressContainer;
import io.github.wimdeblauwe.testcontainers.cypress.CypressTestResults;
import learn.thyme.thyteams.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("integration-test")
public class CypressE2eTests {
    @Container
    private static final PostgreSQLContainer postgreSqlContainer = new PostgreSQLContainer("postgres:12")
            .withDatabaseName("thyteams")
            .withUsername("user")
            .withPassword("secret");

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSqlContainer::getUsername);
        registry.add("spring.datasource.password", postgreSqlContainer::getPassword);
    }

    @BeforeEach
    void validatePreconditions() {
        assertThat(userService.countUsers()).isZero();
    }

    @Test
    void runTests() throws InterruptedException, IOException, TimeoutException {
        /*System.out.println("port = " + port);
        System.out.println("Application started");*/

        /*
        Ensure that the Cypress container can access the app running on port 'port' via 'host.testcontainers.internal'.
        This is a special hostname that allows a Testcontainers container to access the host
        the container is running on.
        The testcontainers-cypress library uses http://host.testcontainers.internal as default base URL.
         */
        org.testcontainers.Testcontainers.exposeHostPorts(port);
        /*
        Declare a CypressContainer with a custom Docker image name so we can match the Cypress
        version with the one we have been using manually before.
         */
        try (CypressContainer container = new CypressContainer("cypress/included:5.1.0")
                                        .withLocalServerPort(port)) {
            container.start();
            CypressTestResults testResults = container.getTestResults();
            assertThat(testResults.getNumberOfFailingTests())
                    .describedAs("%s", testResults)
                    .isZero();
        }
    }
}
