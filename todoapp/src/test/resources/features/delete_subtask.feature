@driver
Feature: Delete Subtask

    Background: The user is logged in and has an existing todo with a subtask
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        And     The user has an existing todo "Buy groceries"
        And     The user has an existing subtask "Get milk" under "Buy groceries"

    Scenario: User can delete a subtask
        When    The user clicks the Delete button on the "Get milk" subtask
        Then    The subtask "Get milk" should no longer appear under the "Buy groceries" todo
