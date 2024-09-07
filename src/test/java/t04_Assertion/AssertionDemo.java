package t04_Assertion;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AssertionDemo {

    WebDriver driver;

    @BeforeClass
    void setup() {
        driver = new ChromeDriver();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.manage().window().maximize();
    }

    @Test
    void logoTest() {
        WebElement logo = driver.findElement(By.xpath("//div[@class='orangehrm-login-logo']"));
        Assert.assertTrue(logo.isDisplayed(), "Logo not displayed");
    }

    @Test
    void pageTitle() {
        Assert.assertEquals(driver.getTitle(), "OrangeHRM!", "Title is not matched");
    }

    @AfterClass
    void teardown() {
        driver.quit();
    }
}
