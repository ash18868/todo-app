@driver
Feature: Delete Todo

    Background: The user is logged in and has an existing todo
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        And     The user has an existing todo "Buy groceries"

    Scenario: User can delete a todo
        When    The user clicks the Delete button on the "Buy groceries" todo
        Then    The todo "Buy groceries" should no longer appear in the todo list
