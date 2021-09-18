History and short Reference
---------------------------

* [Ch03 - Thymeleaf intro](#ch03---thymeleaf-intro)
* [Ch04 - Tailwind CSS](#ch04---tailwind-css)
* [Ch05 - Fragments](#ch05---fragments)
* [Ch06 - Layouts](#ch06---layouts)
* [Ch07 - Controllers](#ch07---controllers)
* [Ch08 - Internationalization](#ch08---internationalization)

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
