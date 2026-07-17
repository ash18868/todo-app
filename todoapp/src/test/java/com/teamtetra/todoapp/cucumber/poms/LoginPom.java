package com.teamtetra.todoapp.cucumber.poms;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPom {

    private WebDriver driver;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(className = "error-message")
    private WebElement statusMessage;

    @FindBy(tagName = "button")
    private WebElement submitButton;

    @FindBy(tagName = "a")
    private WebElement registerLink;



    public LoginPom(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterCredentials(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
    }

    public String getStatusMessage() {
        return statusMessage.getText();
    }

    public void clickSubmitButton() {
        submitButton.click();
    }

    public void clickRegistrationLink(){
        registerLink.click();
    }


}
