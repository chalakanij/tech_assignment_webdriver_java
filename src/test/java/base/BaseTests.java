package base;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.openqa.selenium.Keys.ARROW_DOWN;

public class BaseTests {

    private WebDriver webDriver;
    String BASEURL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";

    @BeforeClass
    public void beforeClass(){
        //System.setProperty("webdriver.chrome.driver","resources/chromedriver.exe");
        System.setProperty("webserver.gecko.driver","resources/geckodriver.exe");
        webDriver = new FirefoxDriver();
    }

    @AfterClass
    public void afterClass(){
       // driver.quit();
    }

    @Test
    public void loginToOrangehrmlive() throws InterruptedException {
        //Navigate to login
        webDriver.navigate().to(BASEURL);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        webDriver.navigate().refresh();

        //Type Username
        webDriver.findElement(By.name("username")).sendKeys("Admin");

        //Type Password
        webDriver.findElement(By.name("password")).sendKeys("admin123");

        //Click Login button
        webDriver.findElement(By.cssSelector(".oxd-button")).click();
        System.out.println(webDriver.getTitle());

    }

    @Test
    public void searchEmployeeByPartialName(String partialName) throws InterruptedException {
        //Click on menu item
        webDriver.findElement(By.linkText("PIM")).click();
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //Type "ch" in the Employee Name text input field
        WebElement employeeNameInput = webDriver.findElement(By.xpath("//div[@id='app']/div/div[2]/div[2]/div/div/div[2]/form/div/div/div/div/div[2]/div/div/input"));
        employeeNameInput.click();
        employeeNameInput.sendKeys(partialName);

        verifyTextPresentInEmployeeNameList(partialName);

        //Click on Search button
        webDriver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    public void waitForElementClickable(By by){
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }


    public void verifyTextPresentInEmployeeNameList(String inputText){
        Actions action =  new Actions(webDriver);
        action.moveToElement(webDriver.findElement(By.xpath("//div[@id='app']/div/div[2]/div[2]/div/div/div[2]/form/div/div/div/div/div[2]/div/div/input"))).sendKeys(ARROW_DOWN).build().perform();

        String text =  webDriver.findElement(By.xpath("//div[@id='app']/div/div[2]/div[2]/div/div/div[2]/form/div/div/div/div/div[2]/div/div/input")).getAttribute("value");
        System.out.println(text);
            if (text.contains(inputText))
              Assert.assertTrue(text.contains(inputText), "Input : " + inputText + " Not Found!");
    }

    public static void main(String args[]) throws InterruptedException {
        BaseTests test = new BaseTests();
        test.beforeClass();
        test.loginToOrangehrmlive();
        test.searchEmployeeByPartialName("ch");
    }
}
