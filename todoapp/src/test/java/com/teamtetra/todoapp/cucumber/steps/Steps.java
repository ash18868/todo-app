package com.teamtetra.todoapp.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.teamtetra.todoapp.cucumber.CucumberRunner;
import com.teamtetra.todoapp.cucumber.poms.LoginPom;
import com.teamtetra.todoapp.cucumber.poms.RegistrationPom;
import com.teamtetra.todoapp.cucumber.poms.DashboardPom;

import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
            assertEquals("Registration successful!", registrationPom.getSuccessMessage());
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
        assertEquals("Registration successful!", registrationPom.getSuccessMessage());
    }

    @Then("The user enters username {string} and password {string}")
    public void The_user_enters_username_and_password(String username, String password) {
        registrationPom.enterCredentials(username, password);
    }

    @Then("The user should see registration failure message {string}")
    public void The_user_should_see_registration_failure_message(String error) {
        assertEquals(error, registrationPom.getErrorMessage());
    }

    @Then("The user should see login failure message {string}")
    public void The_user_should_see_login_failure_message(String error) {
        assertEquals(error, loginPom.getStatusMessage());
    }

    @When("The user clicks the login button")
    public void The_user_clicks_the_login_button() {
        loginPom.clickSubmitButton();
    }

    @Then("The user should be navigated to the dashboard")
    public void The_user_should_be_navigated_to_the_dashboard() {
        assertEquals("Log out?", dashboardPom.getLogoutLink());
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
        assertEquals(title, dashboardPom.getTodoTitle());
    }
  
}