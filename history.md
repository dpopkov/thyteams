History and short Reference
---------------------------

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
