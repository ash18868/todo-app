@driver
Feature: Delete Subtask

    Background: The user is logged in and has an existing todo with a subtask
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        And     The user has an existing todo "Clean the cat"
        And     The user has an existing subtask "Brush the cat" under "Clean the cat"

    Scenario: User can delete a subtask
        When    The user clicks the Delete button on the "Brush the cat" subtask
        Then    The subtask "Brush the cat" should no longer appear under the "Clean the cat" todo
