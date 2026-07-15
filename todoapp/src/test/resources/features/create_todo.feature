@driver
Feature: Create Todo

    Background: The user is logged in and on the dashboard
        Given   The user is logged in with valid credentials
        Then    The user should be navigated to the dashboard
        
    Scenario: User can create a todo with a valid title
        When    The user enters "Buy groceries" in the todo title field
        And     The user clicks the Add todo button
        Then    The todo "Buy groceries" should appear in the todo list
