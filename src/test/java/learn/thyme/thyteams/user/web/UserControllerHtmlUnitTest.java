package learn.thyme.thyteams.user.web;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import learn.thyme.thyteams.infrastructure.security.StubUserDetailsService;
import learn.thyme.thyteams.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
public class UserControllerHtmlUnitTest {
    @Autowired
    private WebClient webClient;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }

    @Test
    @WithUserDetails(StubUserDetailsService.USERNAME_ADMIN)
    void testGetUserAsAdmin() throws IOException {
        when(userService.getUsers(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(
                        Users.createUser(new UserName("Jack", "Sparrow"), "2001-06-19"),
                        Users.createUser(new UserName("John", "Water"), "2001-06-20"),
                        Users.createUser(new UserName("Bob", "Holden"), "2001-06-21")
                )));

        HtmlPage htmlPage = webClient.getPage("/users");
        DomNodeList<DomElement> h1headers = htmlPage.getElementsByTagName("h1");
        assertThat(h1headers).hasSize(1)
                .element(0)
                .extracting(DomElement::asNormalizedText)
                .isEqualTo("Users");

        HtmlTable usersTable = htmlPage.getHtmlElementById("users-table");
        List<HtmlTableRow> rows = usersTable.getRows();

        HtmlTableRow headerRow = rows.get(0);
        assertThat(headerRow.getCell(0).asNormalizedText()).isEqualTo("Name");
        assertThat(headerRow.getCell(1).asNormalizedText()).isEqualTo("Gender");
        assertThat(headerRow.getCell(2).asNormalizedText()).isEqualTo("Birthday");
        assertThat(headerRow.getCell(3).asNormalizedText()).isEqualTo("Email");

        HtmlTableRow row1 = rows.get(1);
        assertThat(row1.getCell(0).asNormalizedText()).isEqualTo("Jack Sparrow");
        assertThat(row1.getCell(1).asNormalizedText()).isEqualTo("MALE");
        assertThat(row1.getCell(2).asNormalizedText()).isEqualTo("2001-06-19");
        assertThat(row1.getCell(3).asNormalizedText()).isEqualTo("jack.sparrow@mail.org");
    }

    @Disabled("Test is disabled because of issues converting MockPart to MultipartFile for avatar file. Need to fix it.")
    @Test
    @WithUserDetails(StubUserDetailsService.USERNAME_ADMIN)
    void testAddUser() throws IOException {
        when(userService.getUsers(any(Pageable.class)))
                .thenReturn(Page.empty());

        HtmlPage htmlPage = webClient.getPage("/users");
        DomNodeList<DomElement> elements = htmlPage.getElementsByTagName("a");
        Optional<DomElement> createUserLink = elements.stream()
                .filter(e -> e.asNormalizedText().equals("Add user"))
                .findFirst();
        assertThat(createUserLink.isPresent());

        HtmlPage createUserFormPage = createUserLink.get().click();
        assertThat(createUserFormPage).isNotNull();

        DomNodeList<DomElement> h1headers = createUserFormPage.getElementsByTagName("h1");
        assertThat(h1headers).hasSize(1)
                .element(0)
                .extracting(DomElement::asNormalizedText)
                .isEqualTo("Create user");

        createUserFormPage.getElementById("gender-MALE").click();
        createUserFormPage.<HtmlTextInput>getElementByName("firstName").setText("John");
        createUserFormPage.<HtmlTextInput>getElementByName("lastName").setText("Millen");
        createUserFormPage.<HtmlEmailInput>getElementByName("email").setText("john.millen@gmail.com");
        createUserFormPage.<HtmlPasswordInput>getElementByName("password").setText("verysecure");
        createUserFormPage.<HtmlPasswordInput>getElementByName("passwordRepeated").setText("verysecure");
        createUserFormPage.<HtmlTextInput>getElementByName("phoneNumber").setText("+555 123 456");
        createUserFormPage.<HtmlTextInput>getElementByName("birthday").setText("2004-03-27");

        HtmlPage pageAfterFormSubmit = createUserFormPage.getElementById("submit-button").click();
        assertThat(pageAfterFormSubmit.getUrl()).isEqualTo(new URL("http://localhost:8080/users"));

        ArgumentCaptor<CreateUserParameters> captor = ArgumentCaptor.forClass(CreateUserParameters.class);
        verify(userService).createUser(captor.capture());

        CreateUserParameters parameters = captor.getValue();
        assertThat(parameters.getUserName().getFirstName()).isEqualTo("John");
        assertThat(parameters.getUserName().getLastName()).isEqualTo("Millen");
        assertThat(parameters.getEmail()).isEqualTo(new Email("john.millen@gmail.com"));
        assertThat(parameters.getPassword()).isEqualTo("verysecure");
        assertThat(parameters.getPhoneNumber()).isEqualTo(new PhoneNumber("+555 123 456"));
        assertThat(parameters.getBirthday()).isEqualTo(LocalDate.parse("2004-03-27"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
            return new StubUserDetailsService(passwordEncoder);
        }

        @Bean
        public ITemplateResolver svgTemplateResolver() {
            SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
            resolver.setPrefix("classpath:/templates/svg/");
            resolver.setSuffix(".svg");
            resolver.setTemplateMode("XML");
            return resolver;
        }
    }
}
