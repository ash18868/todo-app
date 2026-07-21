@driver
Feature: Create Subtask

    Background: The user is logged in and has an existing todo
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        And     The user has an existing todo "Wash car"

    Scenario: User can create a subtask under a todo
        When    The user enters "Buy soap" in the subtask title field for the "Wash car" todo
        And     The user clicks the Add subtask button for the "Wash car" todo
        Then    The subtask "Buy soap" should appear under the "Wash car" todo
    #Todo User cannot add duplicate todos
