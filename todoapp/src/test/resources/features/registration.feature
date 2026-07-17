 @driver
 Feature: Todo App Registration
    
    Background: all users navigate to the registration page
        Given   The user is on the login page
        When    The user clicks the registration link
@current
    Scenario: Users can register with valid credentials
        And     The user enters username "Username" and password "P0ssword"
        And     The user clicks the register button
        Then    The user should see a success message
@current
    Scenario Outline: Users can not register with invalid credentials
        And     The user enters username "<username>" and password "<password>"
        And     The user clicks the register button
        Then    The user should see registration failure message "<message>"

    Examples:
    |username|password|message|
    |shrt|P0ssword|Username should be between 5 and 15 characters|
    |Thisiswaytoolong|P0ssword|Username should be between 5 and 15 characters|
    |Username1|P0ss|Password should be between 5 and 15 characters|
    |Username2|P0sswordP0ssword|Password should be between 5 and 15 characters|
    |Username3|Password|Password requires all special characters|
    |Username4|p0ssword|Password requires all special characters|
    |Username5|P0SSWORD|Password requires all special characters|