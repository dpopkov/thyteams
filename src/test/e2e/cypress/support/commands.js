// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })

Cypress.Commands.add('loginByForm', (username, password) => {
    Cypress.log({
        name: 'loginByForm',
        message: `${username} | ${password}`,
    });

    return cy.request('/login')
        .its('body')
        .then((body) => {
            // we can use Cypress.$ to parse the string body
            // thus enabling us to query into it easily
            const $html = Cypress.$(body);
            const csrf = $html.find('input[name=_csrf]').val();

            cy.loginByCSRF(username, password, csrf)
                .then((resp) => {
                    expect(resp.status).to.eq(200);
                });
        });
});

Cypress.Commands.add('loginByCSRF', (username, password, csrfToken) => {
    cy.request({
        method: 'POST',
        url: '/login',
        failOnStatusCode: false, // dont fail so we can make assertions
        form: true, // we are submitting a regular form body
        body: {
            username,
            password,
            _csrf: csrfToken // insert this as part of form body
        }
    });
});
