@driver
Feature: Create Subtask

    Background: The user is logged in and has an existing todo
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        And     The user has an existing todo "Buy groceries"
    Scenario: User can create a subtask under a todo
        When    The user enters "Get milk" in the subtask title field for the "Buy groceries" todo
        And     The user clicks the Add subtask button for the "Buy groceries" todo
        Then    The subtask "Get milk" should appear under the "Buy groceries" todo
