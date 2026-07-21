@driver
Feature: Create Todo

    Background: The user is logged in and on the dashboard
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard

    Scenario: User can create a todo with a valid title
        When    The user enters "Clean cat litter" in the todo title field
        And     The user clicks the Add todo button
        Then    The todo "Clean cat litter" should appear in the todo list
    #Todo User cannot add duplicate todos
