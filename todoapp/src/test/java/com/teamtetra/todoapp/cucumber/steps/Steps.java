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
import com.teamtetra.todoapp.cucumber.poms.RegistrationPom;

import io.cucumber.java.Before;
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
  // at test time the CucumberRunner will be injected into the constructor so we can get access to
  // the driver and any future test resources
  public Steps(CucumberRunner runner) {
    driver = runner.getDriver();
  }

  @Before
  public void before(){
    if (!setupDone) {
      // Register the shared test user
      driver.get("http://localhost:4200/register");
      driver.findElement(By.id("username")).sendKeys("RegisteredUser");
      driver.findElement(By.id("password")).sendKeys("P0ssword");
      driver.findElement(By.tagName("button")).click();
      driver.findElement(By.cssSelector("p[role='status']"));
      setupDone = true;
    }
  }

  @Given("The user is on the login page")
  public void the_user_is_on_the_login_page() {
    driver.get("http://localhost:4200");// use the "get" method to open a webpage
  }


  @When("The user clicks the registration link")
  public void the_user_clicks_the_registration_link() {
    // we need to first tell Selenium how to find the element we want to interact with
    // we do this by using the findElement method and providing a "By" object. This object
    // tells the driver what the locator strategy is
    WebElement registrationLink = driver.findElement(By.linkText("Register here"));
    // now that we have a Java representation of our element we need to tell the driver what to
    // do with that element. In our case we want to click it, so we use the "click" method
    registrationLink.click();
  }


  
  @When("The user clicks the register button")
  public void the_user_clicks_the_register_button() {
    driver.findElement(By.tagName("button")).click();
  }

  
  @Then("The user should see a success message")
  public void the_user_should_see_a_success_message() {
    // if you have no good options for your By selectors you can always use cssSelector
    WebElement statusMessage = driver.findElement(By.cssSelector("p[role='status']"));
    assertEquals("Registration successful!", statusMessage.getText());
  }


  @Then("The user enters username {string} and password {string}")
  public void The_user_enters_username_and_password(String username, String password) {
    driver.findElement(By.id("username")).sendKeys(username);
    driver.findElement(By.id("password")).sendKeys(password);
  }

  @Then("The user should see failure message {string}")
  public void The_user_should_see_failure_message(String error) {
    WebElement statusMessage = driver.findElement(By.cssSelector("p[class='error-message']"));
    assertEquals(error, statusMessage.getText());
  }

  @When("The user clicks the login button")
  public void The_user_clicks_the_login_button() {
    driver.findElement(By.tagName("button")).click();
  }

  @Then("The user should be navigated to the dashboard")
  public void The_user_should_be_navigated_to_the_dashboard() {
    // Re-query inside the wait so a stale reference after Angular re-renders is retried automatically
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.textToBe(By.tagName("h1"), "To-do or not To-do"));
  }

  @Given("The user is logged in with valid credentials")
  public void The_user_is_logged_in_with_valid_credentials() {
      // Write code here that turns the phrase above into concrete actions
    

    driver.get("http://localhost:4200/");
    driver.findElement(By.id("username")).sendKeys("RegisteredUser");
    driver.findElement(By.id("password")).sendKeys("P0ssword");
    driver.findElement(By.tagName("button")).click();
  }

  @When("The user enters {string} in the todo title field")
  public void The_user_enters_in_the_todo_title_field(String title) {
      // Write code here that turns the phrase above into concrete actions
      driver.findElement(By.id("todo-title")).sendKeys(title);
  }

  @When("The user clicks the Add todo button")
  public void The_user_clicks_the_Add_todo_button() {
      // Write code here that turns the phrase above into concrete actions
      driver.findElement(By.tagName("button")).click();
  }

  @Then("The todo {string} should appear in the todo list")
  public void The_todo_should_appear_in_the_todo_list(String title) {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("label.todo-title")));
      List<WebElement> labels = driver.findElements(By.cssSelector("label.todo-title"));
      boolean found = labels.stream().anyMatch(el -> el.getText().trim().equals(title));
      assertTrue(found, "Expected todo '" + title + "' to appear in the list");
  }

  @And("The user has an existing todo {string}")
  public void The_user_has_an_existing_todo(String title) {
      // Check if the todo already exists before trying to create it
      List<WebElement> existing = driver.findElements(By.cssSelector("label.todo-title"));
      boolean alreadyExists = existing.stream().anyMatch(el -> el.getText().trim().equals(title));
      if (!alreadyExists) {
          driver.findElement(By.id("todo-title")).sendKeys(title);
          driver.findElement(By.cssSelector("section.add-todo button")).click();
          // Wait for the new todo to appear before proceeding
          WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
          wait.until(d -> d.findElements(By.cssSelector("label.todo-title")).stream()
              .anyMatch(el -> el.getText().trim().equals(title)));
      }
  }

  @When("The user clicks the Edit button on the {string} todo")
  public void The_user_clicks_the_Edit_button_on_the_todo(String title) {
      // Find the card whose title label matches, then click its Edit button
      List<WebElement> cards = driver.findElements(By.cssSelector("div.todo-card"));
      for (WebElement card : cards) {
          List<WebElement> labels = card.findElements(By.cssSelector("label.todo-title"));
          if (!labels.isEmpty() && labels.get(0).getText().trim().equals(title)) {
              card.findElement(By.cssSelector("button.btn-edit")).click();
              return;
          }
      }
      throw new RuntimeException("Could not find todo card with title: " + title);
  }

  @When("The user clicks the Delete button on the {string} todo")
  public void The_user_clicks_the_Delete_button_on_the_todo(String title) {
      // Find the card whose title label matches, then click its Delete button
      List<WebElement> cards = driver.findElements(By.cssSelector("div.todo-card"));
      for (WebElement card : cards) {
          List<WebElement> labels = card.findElements(By.cssSelector("label.todo-title"));
          if (!labels.isEmpty() && labels.get(0).getText().trim().equals(title)) {
              card.findElement(By.cssSelector("button.btn-delete")).click();
              return;
          }
      }
      throw new RuntimeException("Could not find todo card with title: " + title);
  }

  @And("The user clears and enters {string} in the edit todo field")
  public void The_user_clears_and_enters_in_the_edit_todo_field(String newTitle) {
      WebElement editInput = driver.findElement(By.cssSelector("input[name='editedTitle']"));
      editInput.clear();
      editInput.sendKeys(newTitle);
  }

  @And("The user clicks the Save button on the todo")
  public void The_user_clicks_the_Save_button_on_the_todo() {
      driver.findElement(By.cssSelector(".edit-todo-form button[type='submit']")).click();
  }

  @Then("The todo {string} should no longer appear in the todo list")
  public void The_todo_should_no_longer_appear_in_the_todo_list(String title) {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      // Wait until no label with that exact text exists in the DOM
      wait.until(ExpectedConditions.invisibilityOfElementLocated(
          By.xpath("//label[contains(@class,'todo-title') and normalize-space()='" + title + "']"))); //Todo: remove xpath
      List<WebElement> labels = driver.findElements(By.cssSelector("label.todo-title"));
      boolean found = labels.stream().anyMatch(el -> el.getText().trim().equals(title));
      assertFalse(found, "Expected todo '" + title + "' to no longer appear in the list");
  }

  @And("The user clicks the Cancel button on the todo")
  public void The_user_clicks_the_Cancel_button_on_the_todo() {
      driver.findElement(By.cssSelector(".edit-todo-form button[type='button']")).click();
  }

  @When("The user enters {string} in the subtask title field for the {string} todo")
  public void The_user_enters_in_the_subtask_title_field_for_the_todo(String subtaskTitle, String todoTitle) {
      WebElement card = findTodoCardByTitle(todoTitle);
      card.findElement(By.cssSelector(".add-subtask-form input[name='title']")).sendKeys(subtaskTitle);
  }

  @And("The user clicks the Add subtask button for the {string} todo")
  public void The_user_clicks_the_Add_subtask_button_for_the_todo(String todoTitle) {
      WebElement card = findTodoCardByTitle(todoTitle);
      card.findElement(By.cssSelector(".add-subtask-form button")).click();
  }

  @Then("The subtask {string} should appear under the {string} todo")
  public void The_subtask_should_appear_under_the_todo(String subtaskTitle, String todoTitle) {
      WebElement card = findTodoCardByTitle(todoTitle);
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      wait.until(d -> card.findElements(By.cssSelector("label.subtask-title")).stream()
          .anyMatch(el -> el.getText().trim().equals(subtaskTitle)));
      List<WebElement> subtaskLabels = card.findElements(By.cssSelector("label.subtask-title"));
      boolean found = subtaskLabels.stream().anyMatch(el -> el.getText().trim().equals(subtaskTitle));
      assertTrue(found, "Expected subtask '" + subtaskTitle + "' to appear under todo '" + todoTitle + "'");
  }

  @And("The user has an existing subtask {string} under {string}")
  public void The_user_has_an_existing_subtask_under(String subtaskTitle, String todoTitle) {
      WebElement card = findTodoCardByTitle(todoTitle);
      // Only create if not already present
      List<WebElement> existing = card.findElements(By.cssSelector("label.subtask-title"));
      boolean alreadyExists = existing.stream().anyMatch(el -> el.getText().trim().equals(subtaskTitle));
      if (!alreadyExists) {
          card.findElement(By.cssSelector(".add-subtask-form input[name='title']")).sendKeys(subtaskTitle);
          card.findElement(By.cssSelector(".add-subtask-form button")).click();
          WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
          wait.until(d -> findTodoCardByTitle(todoTitle)
              .findElements(By.cssSelector("label.subtask-title")).stream()
              .anyMatch(el -> el.getText().trim().equals(subtaskTitle)));
      }
  }

  @When("The user clicks the Edit button on the {string} subtask")
  public void The_user_clicks_the_Edit_button_on_the_subtask(String subtaskTitle) {
      WebElement li = findSubtaskItemByTitle(subtaskTitle);
      li.findElement(By.cssSelector("button.btn-edit")).click();
  }

  @And("The user clears and enters {string} in the edit subtask field")
  public void The_user_clears_and_enters_in_the_edit_subtask_field(String newTitle) {
      WebElement editInput = driver.findElement(By.cssSelector(".edit-subtask-form input[name='editedTitle']"));
      editInput.clear();
      editInput.sendKeys(newTitle);
  }

  @And("The user clicks the Save button on the subtask")
  public void The_user_clicks_the_Save_button_on_the_subtask() {
      driver.findElement(By.cssSelector(".edit-subtask-form button[type='submit']")).click();
  }

  @And("The user clicks the Cancel button on the subtask")
  public void The_user_clicks_the_Cancel_button_on_the_subtask() {
      driver.findElement(By.cssSelector(".edit-subtask-form button[type='button']")).click();
  }

  @When("The user clicks the Delete button on the {string} subtask")
  public void The_user_clicks_the_Delete_button_on_the_subtask(String subtaskTitle) {
      WebElement li = findSubtaskItemByTitle(subtaskTitle);
      li.findElement(By.cssSelector("button.btn-delete")).click();
  }

  @Then("The subtask {string} should no longer appear under the {string} todo")
  public void The_subtask_should_no_longer_appear_under_the_todo(String subtaskTitle, String todoTitle) {
      WebElement card = findTodoCardByTitle(todoTitle);
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      wait.until(d -> card.findElements(By.cssSelector("label.subtask-title")).stream()
          .noneMatch(el -> el.getText().trim().equals(subtaskTitle)));
      List<WebElement> subtaskLabels = card.findElements(By.cssSelector("label.subtask-title"));
      boolean found = subtaskLabels.stream().anyMatch(el -> el.getText().trim().equals(subtaskTitle));
      assertFalse(found, "Expected subtask '" + subtaskTitle + "' to no longer appear under todo '" + todoTitle + "'");
  }

  @Then("The user should see the empty state message {string}")
  public void The_user_should_see_the_empty_state_message(String message) throws IOException, InterruptedException {
      // Extract the JWT from the browser's localStorage
      String token = (String) ((JavascriptExecutor) driver)
          .executeScript("return localStorage.getItem('auth_token');");

      HttpClient http = HttpClient.newHttpClient();

      // Fetch all todos for this user
      HttpRequest getRequest = HttpRequest.newBuilder()
          .uri(URI.create("http://localhost:8080/todo"))
          .header("Authorization", "Bearer " + token)
          .GET()
          .build();
      HttpResponse<String> getResponse = http.send(getRequest, HttpResponse.BodyHandlers.ofString());

      // Parse todoIds from the JSON array and delete each one.
      // Response looks like: [{"todoId":1,"title":"..."},{"todoId":2,...}]
      // Simple regex extraction avoids pulling in a JSON library dependency.
      String body = getResponse.body();
      java.util.regex.Matcher matcher =
          java.util.regex.Pattern.compile("\"todoId\":(\\d+)").matcher(body);
      while (matcher.find()) {
          long todoId = Long.parseLong(matcher.group(1));
          String deleteBody = "{\"todoId\":" + todoId + "}";
          HttpRequest deleteRequest = HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/todo"))
              .header("Authorization", "Bearer " + token)
              .header("Content-Type", "application/json")
              .method("DELETE", HttpRequest.BodyPublishers.ofString(deleteBody))
              .build();
          http.send(deleteRequest, HttpResponse.BodyHandlers.discarding());
      }

      // Reload the dashboard so Angular reflects the now-empty list
      driver.navigate().refresh();
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.empty-state")));
      String actual = driver.findElement(By.cssSelector("p.empty-state")).getText().trim();
      assertEquals(message, actual);
  }

  @Then("The user should see their username displayed in the header")
  public void The_user_should_see_their_username_displayed_in_the_header() {
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.user-greeting")));
      String greetingText = driver.findElement(By.cssSelector("div.user-greeting")).getText();
      assertTrue(greetingText.contains("RegisteredUser"),
          "Expected header to contain 'RegisteredUser' but was: " + greetingText);
  }

  // ── helpers ──────────────────────────────────────────────────────────────

  /**
   * Walks the todo cards and returns the one whose title label text matches.
   * Throws if not found.
   */
  private WebElement findTodoCardByTitle(String title) {
      List<WebElement> cards = driver.findElements(By.cssSelector("div.todo-card"));
      for (WebElement card : cards) {
          List<WebElement> labels = card.findElements(By.cssSelector("label.todo-title"));
          if (!labels.isEmpty() && labels.get(0).getText().trim().equals(title)) {
              return card;
          }
      }
      throw new RuntimeException("Could not find todo card with title: " + title);
  }

  /**
   * Walks all subtask list items across all cards and returns the <li>
   * whose subtask-title label text matches. Throws if not found.
   */
  private WebElement findSubtaskItemByTitle(String subtaskTitle) {
      List<WebElement> items = driver.findElements(By.cssSelector("div.todo-card li"));
      for (WebElement li : items) {
          List<WebElement> labels = li.findElements(By.cssSelector("label.subtask-title"));
          if (!labels.isEmpty() && labels.get(0).getText().trim().equals(subtaskTitle)) {
              return li;
          }
      }
      throw new RuntimeException("Could not find subtask with title: " + subtaskTitle);
  }

}