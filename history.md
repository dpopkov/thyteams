History and short Reference
---------------------------

* [Ch03 - Thymeleaf intro](#ch03---thymeleaf-intro)
* [Ch04 - Tailwind CSS](#ch04---tailwind-css)
* [Ch05 - Fragments](#ch05---fragments)

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
