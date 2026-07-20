package com.teamtetra.todoapp.cucumber.steps;

import com.teamtetra.todoapp.cucumber.CucumberRunner;
import com.teamtetra.todoapp.cucumber.poms.LoginPom;
import com.teamtetra.todoapp.cucumber.poms.RegistrationPom;
import com.teamtetra.todoapp.cucumber.poms.DashboardPom;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

public class Steps {

    // this will store the driver object
    private final WebDriver driver;

    private static boolean setupDone = false;
    
    private RegistrationPom registrationPom;
    private LoginPom loginPom;
    private DashboardPom dashboardPom;
    // at test time the CucumberRunner will be injected into the constructor so we can get access to
    // the driver and any future test resources
    public Steps(CucumberRunner runner) {
        driver = runner.getDriver();
        registrationPom = new RegistrationPom(driver);
        loginPom = new LoginPom(driver);
        dashboardPom = new DashboardPom(driver);
    }

    @Before
    public void before(){
        if (!setupDone) {
        // Register the shared test user
            driver.get("http://localhost:4200/register");
            registrationPom.enterCredentials("test-user", "Password1!");
            registrationPom.clickSubmitButton();
            registrationPom.seeSuccessMessage();
            setupDone = true;
        }
    }

    @Given("The user is on the login page")
    public void the_user_is_on_the_login_page() {
        driver.get("http://localhost:4200");// use the "get" method to open a webpage
    }

    @When("The user clicks the registration link")
    public void the_user_clicks_the_registration_link() {
        loginPom.clickRegistrationLink();
    }
    
    @When("The user clicks the register button")
    public void the_user_clicks_the_register_button() {
        registrationPom.clickSubmitButton();
    }
    
    @Then("The user should see a success message")
    public void the_user_should_see_a_success_message() {
        registrationPom.seeSuccessMessage();
    }

    @Then("The user enters username {string} and password {string}")
    public void The_user_enters_username_and_password(String username, String password) {
        registrationPom.enterCredentials(username, password);
    }

    @Then("The user should see registration failure message {string}")
    public void The_user_should_see_registration_failure_message(String error) {
        registrationPom.seeErrorMessage(error);
    }

    @Then("The user should see login failure message {string}")
    public void The_user_should_see_login_failure_message(String error) {
        loginPom.seeStatusMessage(error);
    }

    @When("The user clicks the login button")
    public void The_user_clicks_the_login_button() {
        loginPom.clickSubmitButton();
    }

    @Then("The user should be navigated to the dashboard")
    public void The_user_should_be_navigated_to_the_dashboard() {
        dashboardPom.seeLogoutLink();
    }

    @Given("The user is logged in with valid credentials")
    public void The_user_is_logged_in_with_valid_credentials() {
        driver.get("http://localhost:4200/login");
        loginPom.enterCredentials("test-user", "Password1!");
        loginPom.clickSubmitButton();
    }

    @When("The user enters {string} in the todo title field")
    public void The_user_enters_in_the_todo_title_field(String title) {
        dashboardPom.enterTodoTitle(title);
    }

    @When("The user clicks the Add todo button")
    public void The_user_clicks_the_Add_todo_button() {
        dashboardPom.clickAddTodoButton();
    }

    @Then("The todo {string} should appear in the todo list")
    public void The_todo_should_appear_in_the_todo_list(String title) {
        dashboardPom.todoExists(title);
    }

    @Then("The user has an existing todo {string}")
    public void The_user_has_an_existing_todo(String title) {
        dashboardPom.seeLogoutLink();
        dashboardPom.enterTodoTitle(title);
        dashboardPom.clickAddTodoButton();
        dashboardPom.todoExists(title);
    }

    @When("The user clicks the Edit button on the {string} todo")
    public void The_user_clicks_the_Edit_button_on_the_todo(String title) {
        dashboardPom.clickTodoEditButton(title);
    }

    @When("The user clears and enters {string} in the edit todo field")
    public void The_user_clears_and_enters_in_the_edit_todo_field(String newTitle) {
        dashboardPom.editTodoTitle(newTitle);
    }

    @When("The user clicks the Save button on the todo")
    public void The_user_clicks_the_Save_button_on_the_todo() {
        dashboardPom.clickSaveButton();
    }

    @Then("The todo {string} should no longer appear in the todo list")
    public void The_todo_should_no_longer_appear_in_the_todo_list(String title) {
        dashboardPom.todoDoesNotExist(title);
    }

    @When("The user clicks the Cancel button on the todo")
    public void The_user_clicks_the_Cancel_button_on_the_todo() {
        dashboardPom.clickCancelButton();
    }

    @When("The user clicks the Delete button on the {string} todo")
    public void The_user_clicks_the_Delete_button_on_the_todo(String title) {
        dashboardPom.clickTodoDeleteButton(title);
    }

    @When("The user enters {string} in the subtask title field for the {string} todo")
    public void The_user_enters_in_the_subtask_title_field_for_the_todo(String subtaskTitle, String todoTitle) {
        dashboardPom.enterSubtaskTitle(todoTitle, subtaskTitle);
    }

    @When("The user clicks the Add subtask button for the {string} todo")
    public void The_user_clicks_the_Add_subtask_button_for_the_todo(String todoTitle) {
        dashboardPom.clickAddSubtaskButton(todoTitle);
    }

    @Then("The subtask {string} should appear under the {string} todo")
    public void The_subtask_should_appear_under_the_todo(String subtaskTitle, String todoTitle) {
        dashboardPom.subtaskExists(subtaskTitle, todoTitle);
    }

    @Then("The user has an existing subtask {string} under {string}")
    public void The_user_has_an_existing_subtask_under(String subtaskTitle, String todoTitle) {
        dashboardPom.enterSubtaskTitle(todoTitle, subtaskTitle);
        dashboardPom.clickAddSubtaskButton(todoTitle);
        dashboardPom.subtaskExists(subtaskTitle, todoTitle);
    }

    @When("The user clicks the Edit button on the {string} subtask")
    public void The_user_clicks_the_Edit_button_on_the_subtask(String subtaskTitle) {
        dashboardPom.clickSubtaskEditButton(subtaskTitle);
    }

    @When("The user clears and enters {string} in the edit subtask field")
    public void The_user_clears_and_enters_in_the_edit_subtask_field(String newSubtaskTitle) {
        dashboardPom.editSubtaskTitle(newSubtaskTitle);
    }

    @When("The user clicks the Save button on the subtask")
    public void The_user_clicks_the_Save_button_on_the_subtask() {
        dashboardPom.clickSaveButton();
    }

    @Then("The subtask {string} should no longer appear under the {string} todo")
    public void The_subtask_should_no_longer_appear_under_the_todo(String subtaskTitle, String todoTitle) {
        dashboardPom.subtaskDoesNotExist(subtaskTitle);
    }

    @When("The user clicks the Cancel button on the subtask")
    public void The_user_clicks_the_Cancel_button_on_the_subtask() {
        dashboardPom.clickCancelButton();
    }

    @When("The user clicks the Delete button on the {string} subtask")
    public void The_user_clicks_the_Delete_button_on_the_subtask(String subtaskTitle) {
        dashboardPom.clickSubtaskDeleteButton(subtaskTitle);
    }

}