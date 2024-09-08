package t09_parallelTesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ParallelTestingDemo {

    WebDriver driver;

    @BeforeTest
    void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @AfterTest
    void teardown() {
        driver.quit();
    }

    @Test
    void logoTest() throws InterruptedException {
        WebElement logo = driver.findElement(By.xpath("//div[@class='orangehrm-login-logo']/img"));
        Assert.assertTrue(logo.isDisplayed());
        Thread.sleep(4000);
    }

    @Test
    void homepageTitle() throws InterruptedException {
        Assert.assertEquals(driver.getTitle(), "OrangeHRM");
        Thread.sleep(4000);
    }
}
