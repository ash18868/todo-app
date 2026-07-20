@driver
Feature: Delete Todo

    Background: The user is logged in and has an existing todo
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        And     The user has an existing todo "Vacuum bedroom"

    Scenario: User can delete a todo
        When    The user clicks the Delete button on the "Vacuum bedroom" todo
        Then    The todo "Vacuum bedroom" should no longer appear in the todo list
