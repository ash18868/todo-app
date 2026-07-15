package com.teamtetra.todoapp.cucumber.poms;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/*
    A common Selenium (really any web automation) design pattern is the Page Object Model (POM)
    design pattern. You use a class to represent your web page and provide easy to access resources
    that let you find and interact with common web elements on the page. This lets you centralize
    any actions you need to perform in one location you can then reuse wherever your perform your
    automation
*/
public class RegistrationPom {

  private WebDriver driver;

  @FindBy(id = "username")
  private WebElement usernameInput;

  @FindBy(id = "password")
  private WebElement passwordInput;

  @FindBy(className = "error-message")
  private WebElement statusMessage;

  public RegistrationPom(WebDriver driver) {
    this.driver = driver;
    /*
        The main benefit of Selenium POMs is we can configure Selenium to automatically find
        the web elements we want to interact with on the page and create a WebElement object
        of them for us whenever we reference the field associated with that web element. The
        PageFactory is the tool we use to configure this. We simply need to provide a reference
        to the driver the PageFactory should use and a reference to the POM itself (via "this" keyword)

        Note this also protects us from "stale elements". These are WebElements that were created when a web page was in
        a previous state. Even if they are still valid locators Selenium does not want to take the chance of causing unintended
        side effects and will throw an error if you try to interact with a stale element. The PageFactory prevents this from
        happening
    */
    PageFactory.initElements(driver, this);
  }

  // Once you have your fields set you can create helper methods to perform common actions on your
  // web page

  public void enterCredentials(String username, String password) {
    usernameInput.sendKeys(username);
    passwordInput.sendKeys(password);
  }

  public String getStatusMessage() {
    return statusMessage.getText();
  }
}