@driver
Feature: Edit Subtask

    Background: The user is logged in and has an existing todo with a subtask
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        And     The user has an existing todo "Buy groceries"
        And     The user has an existing subtask "Get milk" under "Buy groceries"

    Scenario: User can edit a subtask title
        When    The user clicks the Edit button on the "Get milk" subtask
        And     The user clears and enters "Get oat milk" in the edit subtask field
        And     The user clicks the Save button on the subtask
        Then    The subtask "Get oat milk" should appear under the "Buy groceries" todo
        And     The subtask "Get milk" should no longer appear under the "Buy groceries" todo

    Scenario: User can cancel editing a subtask
        When    The user clicks the Edit button on the "Get milk" subtask
        And     The user clicks the Cancel button on the subtask
        Then    The subtask "Get milk" should appear under the "Buy groceries" todo
