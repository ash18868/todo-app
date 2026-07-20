@driver
Feature: View Todos and Subtasks on Dashboard

    Background: The user is logged in
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard

    Scenario: User can see their todos on the dashboard
        Given   The user has an existing todo "Buy groceries"
        Then    The todo "Buy groceries" should appear in the todo list

    Scenario: User can see subtasks nested under their todo
        Given   The user has an existing todo "Buy groceries"
        And     The user has an existing subtask "Get milk" under "Buy groceries"
        Then    The subtask "Get milk" should appear under the "Buy groceries" todo