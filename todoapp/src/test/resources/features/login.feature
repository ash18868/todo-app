@driver
Feature: Todo App Login

    Background: All users already have a registed account
        Given   The user is on the login page


    Scenario: Users can login with valid credentials
        When    The user enters username "RegisterUser" and password "P0ssword"
        And     The user clicks the login button
        Then    The user should be navigated to the dashboard


    Scenario Outline: Users can not login with invalid credentials
        When    The user enters username "<username>" and password "<password>"
        And     The user clicks the login button
        Then    The user should see failure message "<message>"


    Examples:
    |username|password|message|
    |IncorrectUser|P0ssword|Invalid username or password|
    |Username|Inc0rrectPass|Invalid username or password|