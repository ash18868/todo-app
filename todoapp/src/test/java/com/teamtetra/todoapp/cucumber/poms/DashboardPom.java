package com.teamtetra.todoapp.cucumber.poms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
    

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

    public void todoExists(String title){
        findCardByTitle(title);
    }

    public boolean todoDoesNotExist(String title){
         WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        return wait.until(webDriver -> webDriver.findElements(By.cssSelector(".todo-card"))
                .stream()
                .noneMatch(card -> title.equals(card.getAttribute("data-todo-title"))));
    }

    public void clickEditButton(String title){
        WebElement card = findCardByTitle(title);
        card.findElement(By.cssSelector(".card-row > .card-actions .btn-edit")).click();
    }

    public void editTodoTitle(String newTitle){
        WebElement editTitleInput = driver.findElement(By.name("editedTitle"));
        editTitleInput.clear();
        editTitleInput.sendKeys(newTitle);
    }

    public void clickSaveButton(){
        //Find the save button
        WebElement saveButton = driver.findElement(By.id("save-btn"));
        saveButton.click();
    }

    public WebElement findCardByTitle(String title){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        return wait.until(webDriver -> webDriver.findElements(By.cssSelector(".todo-card"))
            .stream()
            .filter(card -> title.equals(card.getAttribute("data-todo-title")))
            .findFirst()
            .orElse(null));
    }

    public void clickCancelButton(){
        WebElement cancelButton = driver.findElement(By.id("cancel-btn"));
        cancelButton.click();
    }

    public void clickDeleteButton(String title){
        WebElement card = findCardByTitle(title);
        card.findElement(By.cssSelector(".card-row > .card-actions .btn-delete")).click();
    }
}
