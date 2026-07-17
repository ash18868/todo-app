package com.teamtetra.todoapp.cucumber.poms;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
    

public class DashboardPom {
    
    private WebDriver driver;

    @FindBy(className = "logout-link")
    private WebElement logoutLink;

    @FindBy(tagName = "input")
    private WebElement titleInput;

    @FindBy(id = "btn-add")
    private WebElement addTodoButton;

    @FindBy(className = "todo-title")
    private WebElement todoTitle;

    public DashboardPom(WebDriver driver) {

        this.driver = driver;
        PageFactory.initElements(driver, this);

    }

    public String getLogoutLink() {
        return logoutLink.getText();
    }

    public void enterTodoTitle(String title){
        titleInput.sendKeys(title);
    }

    public void clickAddTodoButton(){
        addTodoButton.click();
    }

    public String getTodoTitle(){
        return todoTitle.getText();
    }

}
