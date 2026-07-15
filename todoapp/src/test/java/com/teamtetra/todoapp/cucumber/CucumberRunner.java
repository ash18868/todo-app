package com.teamtetra.todoapp.cucumber;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import com.teamtetra.todoapp.cucumber.poms.RegistrationPom;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import java.time.Duration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@Suite // This tells Junit the class facilitates other test classes
@IncludeEngines("cucumber") // This tells Junit to let Cucumber facilitate the tests associated with this class
@SelectPackages({"features", "com.teamtetra.todoapp.cucumber"}) // This tells junit to include the features directory of "resources" and the steps package as part of the suite
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.teamtetra.todoapp.cucumber") // This tells CUCUMBER where the code associated with the acceptance criteria is located
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "html:reports/cucumber-report.html") // This tells Cucumber to create an html test report
@ConfigurationParameter(key = "cucumber.filter.tags", value = "@current")
@CucumberContextConfiguration // This tells Spring to manage dependency injection for Cucumber
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // spins up the application web server for testing
@TestPropertySource(locations = "classpath:test.properties") // tells Spring to use our test properties
public class CucumberRunner {

  
  public WebDriver driver;// this object will control our web interactions
  
  private RegistrationPom registrationPom;// this object will facilitate registration page actions

  private static boolean setupDone = false;

  @Before
  public void setup() {
    ChromeOptions options = new ChromeOptions();
    // options.addArguments("--headless");
    options.addArguments("--start-maximized");

    driver = new ChromeDriver(options);// This initializes a web driver that interacts with Chrome
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));// This tells Selenium to wait up to the given time for an element to be found on the web page
    registrationPom = new RegistrationPom(driver);

    
  }

  @After
  public void teardown() {
    /*
        The webdriver is a third party tool that is not limited to the lifecycle of our Java
        tests. This means we must manually shut it down when our tests are done
    */
    if (driver != null) {
      driver.quit();
      driver = null;
    }
  }

  // we need to give ourselves access to the driver
  public WebDriver getDriver() {
    return driver;
  }

  public RegistrationPom getRegistrationPom() {
    return registrationPom;
  }
}