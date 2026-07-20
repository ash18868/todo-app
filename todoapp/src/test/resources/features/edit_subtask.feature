@driver
Feature: Edit Subtask

    Background: The user is logged in and has an existing todo with a subtask
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        And     The user has an existing todo "Take out trash"
        And     The user has an existing subtask "Gather trash" under "Take out trash"

    Scenario: User can edit a subtask title
        When    The user clicks the Edit button on the "Gather trash" subtask
        And     The user clears and enters "Empty trash bins" in the edit subtask field
        And     The user clicks the Save button on the subtask
        Then    The subtask "Empty trash bins" should appear under the "Take out trash" todo
        And     The subtask "Gather trash" should no longer appear under the "Take out trash" todo

    Scenario: User can cancel editing a subtask
        When    The user clicks the Edit button on the "Gather trash" subtask
        And     The user clicks the Cancel button on the subtask
        Then    The subtask "Gather trash" should appear under the "Take out trash" todo
