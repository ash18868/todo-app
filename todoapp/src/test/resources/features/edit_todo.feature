@driver
Feature: Edit Todo

    Background: The user is logged in and has an existing todo
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        And     The user has an existing todo "Buy groceries"

    Scenario: User can edit a todo title
        When    The user clicks the Edit button on the "Buy groceries" todo
        And     The user clears and enters "Buy milk" in the edit todo field
        And     The user clicks the Save button on the todo
        Then    The todo "Buy milk" should appear in the todo list
        And     The todo "Buy groceries" should no longer appear in the todo list

    Scenario: User can cancel editing a todo
        When    The user clicks the Edit button on the "Buy groceries" todo
        And     The user clicks the Cancel button on the todo
        Then    The todo "Buy groceries" should appear in the todo list
    #Todo User cannot edit todos to make duplicate todos