package com.teamtetra.todoapp.cucumber.poms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
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

    public void seeLogoutLink() {
        assertEquals("Log out?", logoutLink.getText());
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
        findTodoCardByTitle(title);
    }

    public void todoDoesNotExist(String title){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        boolean check = wait.until(webDriver -> webDriver.findElements(By.cssSelector(".todo-card"))
                .stream()
                .noneMatch(card -> title.equals(card.getAttribute("data-todo-title"))));
        assertEquals(true, check);
    }

    public void clickTodoEditButton(String title){
        WebElement card = findTodoCardByTitle(title);
        card.findElement(By.cssSelector(".card-row > .card-actions .btn-edit")).click();
    }

    public void editTodoTitle(String newTitle){
        WebElement editTitleInput = driver.findElement(By.name("editedTitle"));
        editTitleInput.clear();
        editTitleInput.sendKeys(newTitle);
    }

    public void clickSaveButton(){
        //Find the save button
        WebElement saveButton = driver.findElement(By.className("btn-save"));
        saveButton.click();
    }

    public WebElement findTodoCardByTitle(String title){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        return wait.until(webDriver -> webDriver.findElements(By.cssSelector(".todo-card"))
            .stream()
            .filter(card -> title.equals(card.getAttribute("data-todo-title")))
            .findFirst()
            .orElse(null));
    }

    public WebElement findSubtaskCardByTitle(String title){
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        return wait.until(webDriver -> webDriver.findElements(By.cssSelector(".subtask-card"))
            .stream()
            .filter(card -> title.equals(card.getAttribute("data-subtask-title")))
            .findFirst()
            .orElse(null));
    }

    public void clickCancelButton(){
        WebElement cancelButton = driver.findElement(By.className("btn-cancel"));
        cancelButton.click();
    }

    public void clickTodoDeleteButton(String title){
        WebElement card = findTodoCardByTitle(title);
        card.findElement(By.cssSelector(".card-row > .card-actions .btn-delete")).click();
    }

    public void enterSubtaskTitle(String todoTitle, String subtaskTitle){
        WebElement card = findTodoCardByTitle(todoTitle);
        WebElement subtaskTitleInput = card.findElement(By.id("title"));
        subtaskTitleInput.sendKeys(subtaskTitle);
    }

    public void clickAddSubtaskButton(String todoTitle){
        WebElement card = findTodoCardByTitle(todoTitle);
        WebElement addSubtaskButton = card.findElement(By.id("add-subtask-btn"));
        addSubtaskButton.click();
    }

    public void subtaskExists(String subtaskTitle, String todoTitle){
        WebElement card = findTodoCardByTitle(todoTitle);
        WebElement subtask = findSubtaskCardByTitle(subtaskTitle);
        assertEquals(subtaskTitle, subtask.getAttribute("data-subtask-title"));
    }

    public void clickSubtaskEditButton(String subtaskTitle){
        WebElement card = findSubtaskCardByTitle(subtaskTitle);
        WebElement editButton = card.findElement(By.className("btn-edit"));
        editButton.click();
    }

    public void editSubtaskTitle(String newSubtaskTitle){
        WebElement editTitleInput = driver.findElement(By.name("editedTitle"));
        editTitleInput.clear();
        editTitleInput.sendKeys(newSubtaskTitle);
    }

    public void subtaskDoesNotExist(String subtaskTitle){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        boolean check = wait.until(webDriver -> webDriver.findElements(By.cssSelector(".subtask-card"))
                .stream()
                .noneMatch(card -> subtaskTitle.equals(card.getAttribute("data-subtask-title"))));
        assertEquals(true, check);
    }

    public void clickSubtaskDeleteButton(String subtaskTitle){
        WebElement card = findSubtaskCardByTitle(subtaskTitle);
        card.findElement(By.className("btn-delete")).click();
    }
}
