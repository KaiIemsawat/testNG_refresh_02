package t05_parameters;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParametersDemo {

    WebDriver driver;

    @BeforeClass
    @Parameters({"browser", "url"})
    void setup(String browser, String url) {

        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("chrome")) {
            driver = new FirefoxDriver();
        }

        driver.get(url);
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    void logoTest() {
        WebElement logo = driver.findElement(By.xpath("//div[@class='orangehrm-login-logo']"));
        Assert.assertTrue(logo.isDisplayed(), "Logo not displayed");
    }

    @Test(priority = 2)
    void pageTitle() {
        Assert.assertEquals(driver.getTitle(), "OrangeHRM", "Title is not matched");
    }

    @AfterClass
    void teardown() {
        driver.quit();
    }
}
