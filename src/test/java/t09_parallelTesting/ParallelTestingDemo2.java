package t09_parallelTesting;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import t08_XlsxData.XlsxData;

public class ParallelTestingDemo2 {
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
    public void loginTest() throws InterruptedException {
        Thread.sleep(1500);

        driver.findElement(By.xpath("//input[@name='username']")).sendKeys("Admin");
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys("admin123");
        driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--main orangehrm-login-button']")).click();

        Thread.sleep(3000);

        String dashboardText = driver.findElement(By.xpath("//h6[@class='oxd-text oxd-text--h6 oxd-topbar-header-breadcrumb-module']")).getText();
        Assert.assertTrue(dashboardText.equals("Cockpit") || dashboardText.equals("Dashboard"));

        Thread.sleep(2000);
    }

}
