History and short Reference
---------------------------

* [Ch03 - Thymeleaf intro](#ch03---thymeleaf-intro)
* [Ch04 - Tailwind CSS](#ch04---tailwind-css)
* [Ch05 - Fragments](#ch05---fragments)
* [Ch06 - Layouts](#ch06---layouts)
* [Ch07 - Controllers](#ch07---controllers)
* [Ch08 - Internationalization](#ch08---internationalization)
* [Ch09 - Database connection](#ch09---database-connection)
* [Ch10 - Displaying data](#ch10---displaying-data)
* [Ch11 - Forms](#ch11---forms)
* [Ch12 - Data editing](#ch12---data-editing)
* [Ch13 - Deletion of an entity](#ch13---deletion-of-an-entity)
* [Ch14 - Security](#ch14---security)
* [Ch15 - Testing](#ch15---testing)
* [Ch16 - Various Tips](#ch16---various-tips)

### Ch03 - Thymeleaf intro
* First controller and view
#### Thymeleaf expressions
* Map: `th:text="${capitalsOfTheWorld.Belguim}"`
* Map: `th:text="${capitalsOfTheWorld['The Netherlands']}"`
* Array or List: `th:text="${vehiclesList[0].name}"`
* Use properties for text (for l10n): `th:text="#{dashboard.title}"`
    * when messages.properties contains `dashboard.title=Dashboard`
* Selected objects:
```html
<div th:object="${car}">
    <p>Brand: <span th:text="*{brand}"></span></p>
    <p>Type: <span th:text="*{type}"></span></p>
<p>
```
##### Links to URLs
* absolute: `<a th:href="@{https://www.google.com/search?q=thymeleaf"></a>`
* relative: `<a th:href="@{/users}"></a>`
* with context variables: `<a th:href="@{https://www.google.com/search(q=${searchTerm})"></a>`
* with path variables: `<a th:href="@{/users/{userId}/edit(userId=${user.id})"></a>`
##### Literal substitutions
* Combine string literal: `<div th:id="'container-' + ${index}"></div>`
* Shortcut pipe syntax: `<div th:id="|container-${index}|"></div>`
##### Expression inlining
`<span>The total price is [[${totalPrice}]]</span>`

#### Thymeleaf attributes
* Element text content - __th:text__
* Element id attribute - __th:id__
* Conditional inclusion - __th:if__
* Conditional exclusion - __th:unless__
* Iteration - __th:each="item : ${items}"__

#### Preprocessing
* The Example `<h1 th:text="#{__${title}__}"></h1>`
    * If title=users.title
    * Then the example turns into `<h1 th:text="#{users.title}"></h1>`
    * Then Thymeleaf will search for the key `users.title` in L10n property files 

### Ch04 - Tailwind CSS
* Create package.json
```json
{
"name": "taming-thymeleaf-app"
}
```
* Install Tailwind: `npm install tailwindcss@latest postcss@latest autoprefixer@latest`
* Create postcss.config.js
```javascript
module.exports = {
    plugins: {
        tailwindcss: {},
        autoprefixer: {}
    }
}
```
* Update application.css:
```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```
* Install Gulp: `npm install --global gulp-cli`
* Install Gulp as dependency: 
```
npm install --save-dev gulp gulp-watch browser-sync gulp-babel \
@babel/core @babel/preset-env \
gulp-terser gulp-uglifycss gulp-postcss gulp-purgecss gulp-environments
```
* Create gulpfile.js
* Add scripts to package.json
* Run `npm run build`
* Generate default config file: `npx tailwind init`
* Update generated tailwind.config.js
* Build for production: `npm run build-prod`
* Configure Maven with frontend-maven-plugin

### Ch05 - Fragments

#### What is fragment
You define a fragment using __th:fragment__
```html
<div th:fragment="separator">
    <div class="border-dashed border-2 border-red-300 mx-4">
    </div>
</div>
```
#### Using fragments
We can reference the specific separator fragment using this __~{filename :: fragmentname}__ syntax:
```html
<div th:insert="~{fragments :: separator}"></div>
```
or for non-complex fragment expression:
```html
<div th:insert="fragments :: separator"></div>
```
#### Fragments with parameters
```html
<a th:fragment="menu-item(title, link)"
    th:text="${title}"
    th:href="${link}"
    class="flex items-center px-2 py-2 text-base leading-6 font-medium text-gray-900">
</a>
```
can be used like this:
```html
<a th:replace="fragments :: menu-item('Users', '/users')"></a>
<a th:replace="fragments :: menu-item('Groups', '/groups')"></a>
```
#### Inline separate SVG files
* Create templates/svg directory and put SVG files in it
* Add resolver for SVG files
* Set `spring.thymeleaf.template-resolver-order=0`

### Ch06 - Layouts
* Add dependency for `thymeleaf-layout-dialect`
* Add `templates/layout/layout.html`
* Refactor `index.html`

### Ch07 - Controllers
* Add UserController, TeamController
* Add views teams/list, users/list
* Add svg files and adjust the sidebar menu
* Add RootController which redirects to /users

### Ch08 - Internationalization
* Set application property `spring.messages.basename=i18n/messages`
* Add properties files containing translated messages
* Refer to the translated text by using the key with the __#{...}__ syntax in the __th:text__ attribute
* Add WebMvcConfigurer with a LocaleChangeInterceptor
* Open `http://localhost:8080/users?lang=nl` in the browser to view the Dutch translation
* Remove the cookie or add `/?lang=en` to the URL to go back to the English translations

### Ch09 - Database connection
* Add Spring Data JPA to the project
* Add "JPA Early Primary Key Library" to
    * implement early primary key generation before storing the object to the database
        * objects have primary keys since the moment of construction
        * it makes implementing equals() and hashCode() simpler
    * use dedicated primary key classes
* Add `<pluginGroup>io.github.wimdeblauwe</pluginGroup>` to ~/.m2/settings.xml
* Generate the basic structure of an entity and JPA Repository:
```
mvn jpearl:generate -Dentity=User
# It will generate User, UserId, UserRepository, UserRepositoryCustom, UserRepositoryImpl, UserRepositoryTest.
```
* Expose UniqueIdGenerator bean in ThymeleafApplicationConfiguration
* Use Flyway to create table
    * Add dependency for `<artifactId>flyway-core</artifactId>`
    * Add migration script
    * Tell Hibernate to use the specific table name (using @Table annotation)
* Specify the database to run Integration Tests against
    * Add dependencies for `testcontainers`
    * Add dependency for PostgreSQL driver
    * Configure UserRepositoryTest for PostgreSQL db started via Testcontainers
    * Set properties for test in an application-data-jpa-test.properties file

### Ch10 - Displaying data
* Add DatabaseInitializer to generate random users
* Add UserService and UserServiceImpl
* Create `application-local.properties` for Postgres db running in Docker
* Start database in docker using: `docker-compose up -d`
* Run the application with the local, init-db (for the 1st time only) profiles
* Display list of users on the html page
* Add Pagination
    * Replace CrudRepository with PagingAndSortingRepository
    * Add Thymeleaf fragment 'controls' for pagination

### Ch11 - Forms
* Add CreateUserFormData value class and implement createUserForm and doCreateUser methods in controller
* Create NotExistingUser annotation and NotExistingUserValidator
* Add LocalValidatorFactoryBean to app configuration
* Use validation groups to influence the processing order of the validations

### Ch12 - Data editing
* Create "Add user" button
* Update SQL script to have a new "version" field, which will allow using Optimistic Locking
* Drop and re-create schema in db
* Replace AbstractEntity with AbstractVersionedEntity in entity User
* Add methods getUser and editUser to UserService
* Add EditUserFormData class
* Add method editUserForm to UserController
* Create StringToUserIdConverter
* Make the Edit links in the list of users point to the proper URL
* Update edit.html to the take the editMode and version into account
* Implementing the actual save operation via a POST call in the controller
* Split ValidationGroupSequence to two sequences for create and edit operations
* Create `textinput` fragment for form
* Create an @ControllerAdvice annotated GlobalControllerAdvice class that will handle the Optimistic Locking failure exception
* Add html views for errors

### Ch13 - Deletion of an entity
* Delete using a dedicated URL
    * Add method deleteUser to UserService
    * Add a new POST mapping to UserController
    * Add flash attributes to show a confirmation message after the redirect to the list of users

### Ch14 - Security
* Add dependency for `spring-boot-starter-security`
* Add WebSecurityConfiguration extends WebSecurityConfigurerAdapter
* Add PasswordEncoder bean to application config
* Add @EnableGlobalMethodSecurity to security config to enabe the use of specific annotations on the controller methods
* Use @Secured on controller methods
* Use Thymeleaf Spring Security integration:
    * dependency for `thymeleaf-extras-springsecurity5`
    * edit templates to take the current user role into account
    * show current logged on user information on top of the page
* Create custom logon page
    * Add login template
    * Add LoginController
    * Update WebSecurityConfiguration to use custom /login page
* Make sign out in top menu work
* Users from database
    * enum UserRole
    * update User entity by adding roles and password fields, factory methods
    * change the Flyway migration script and clear the db
    * update UserService, CreateUserParameters, DatabaseInitializer
    * connect User entity with Security - create DatabaseUserDetailsService, ApplicationUserDetails
    * update WebSecurityConfiguration
    * run 1st time with init-db profile
    * show current user info in Profile dropdown panel
    * update Edit view, UserController

### Ch15 - Testing
* Using @WebMvcTest
    * UserControllerTest
        * test redirecting non-authenticated user to /login
        * test a user with the appropriate authorization can access the application
* Using HtmlUnit
* Using Cypress
    * Install Cypress
        * Create src/test/e2e folder
        * Create package.json
        * Open a terminal at src/test/e2e and run: `npm install cypress --save-dev`
        * Run `npx cypress open`. It will start the browser and run the generated example tests.
    * Prepare for Cypress tests
        * Create a @RestController IntegrationTestController so we can call the endpoints from the Cypress tests.
        * These endpoints should only be started when running as a test. 
        * It is very important that this is not exposed when running on production as it wipes the complete database.
        * Update WebSecurityConfiguration to allow everybody to access `/api/integration-test`
        * Create `auth.spec.js` test, update `cypress.json` to set the base url.
        * Run the application with profiles `local` and `integration-test`.
        * Run `npx cypress open`. Click on `auth.spec.js` in Cypress desktop application.
        * Add `commands.js` and `user-management.spec.js` test
    * Run Cypress tests from JUnit
        * Add dependencies for `org.testcontainers` artifacts `junit-jupiter` and `testcontainers-cypress`
        * Create CypressE2eTests
            * Create e2e test template
                * @SpringBootTest starts the complete application.
                * The @Testcontainers annotation triggers the JUnit 5 support of Testcontainers.
                * The @Container annotation will start and stop the Docker container via Testcontainers.
                * Use @LocalServerPort to inject the port.
                * Dynamically set the JDBC URL, db username and password by adding method annotated with @DynamicPropertySource.
                * The database should be empty at startup.
                * Run (in the src/test/e2e directory): `npm install cypress-multi-reporters mocha mochawesome --save-dev`
                * Update cypress.json to add reporter and reporterOptions
                * Create reporter-config.json
                * Update .gitignore to avoid accidental commits
                * Update the Maven pom.xml so the Cypress tests are made available as test resources
            * Start the Cypress Docker container in our JUnit test and run the Cypress tests
                * Ensure the `integration-test` profile is active so integration-test REST endpoint will be available.
                * Ensure that the Cypress container can access the app running on port via http://host.testcontainers.internal 
                * Declare a CypressContainer with a custom Docker image name.
                * Start the container programmatically, get the results, and assert that there should be no failing tests.
            * Use the JUnit 5 support for dynamic tests
                * Replace @Test with @TestFactory to indicate that this method will return a collection of tests.
                * Change the return type from void to List<DynamicContainer>.
                * Convert the test results from the CypressContainer to a List<DynamicContainer>

### Ch16 - Various Tips
* Set Open Session in View false: `spring.jpa.open-in-view=false`.
* Use StringTrimmerEditor to remove excess whitespaces when the values are taken from the \<input\> fields.
* Add methods annotated with @ModelAttribute to use controller specific model attributes. 
* Use @ControllerAdvice to have model attributes across the whole application.
    * Update `resources` section in pom.
    * Use version in Thymeleaf templates (e.g. login.html)
* File upload
    * Use MultipartFile field
    * Update User entity and Flyway sql script.
    * Update UserServiceImpl, parameter and form classes.
    * Update edit.html to allow the user to select a file.
* Selecting a linked entity value
    * Create Team entity using JPearl: `mvn jpearl:generate -Dentity=Team`
    * Add fields to Team.
    * Create `V1.1__add-team.sql` migration sql script.
    * Create TeamService.
    * Create CreateTeamFormData, EditTeamFormData, StringToTeamIdConverter, edit.html view template.
    * Update TeamController, UserService, list.html view template.
    * Update DatabaseInitializer to populate teams.
    * Add e2e tests for Team operations:
        * add `team-management.spec.js`
        * add a new endpoint to IntegrationTestController so there is a team present
        * make a 'hack' - add id `success-alert-message` for `success' fragment - need to FIX it!
* Dynamically adding rows
    * Add TeamPlayer entity, update Team for TeamPlayer-s.
    * Update TeamRepositoryCustom for TeamPlayerId.
    * Update Flyway sql script, TeamRepositoryTest.
    * Add TeamPlayerFormData, update CreateTeamFormData
    * Create teamplayer-form fragment, update edit.html for Players
    * Update TeamController for positions
    * Update DatabaseInitializer for players, update EditTeamFormData for players
    * Update TeamService for addPlayer and getTeamWithPlayers methods.
    * Update TeamRepository for findTeamWithPlayers method
    * Add TeamPlayerParameters, CreateTeamParameters, EditTeamParameters.
    * Update TeamService to use parameter classes and persist players.
    * Add rows:
        * Update edit and edit-teamplayer-fragment.
        * Add a method to TeamController that returns the fragment.
    * Delete rows:
        * Update the edit-teamplayer-fragment.
        * Add removeTeamPlayerForm js function.
        * Add RemoveUnusedTeamPlayersValidator to TeamController.
        * Update CreateTeamFormData.
* Custom property formatters
    * Add PhoneNumberFormatter and configure it in WebMvcConfiguration
    * Update AbstractUserFormData
    * Move validation logic to PhoneNumber
* Date picker (Duet Date Picker)
    * Add JavaScript and CSS to layout.html
    * Create dateinput component in the forms.html fragments
