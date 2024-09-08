package t08_XlsxData;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class XlsxDataTestDemo {
    WebDriver driver;

    @BeforeTest
    public void setup() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        Thread.sleep(3000);
    }

    @AfterTest
    public void teardown() {
        driver.quit();
    }

    // If the name of dataProvider is not specifically assign, it's method name by default
    @Test(dataProvider = "getData" , dataProviderClass = XlsxData.class)
    public void loginTest(String username, String password) throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='username']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--main orangehrm-login-button']")).click();

        Thread.sleep(3000);

        String dashboardText = driver.findElement(By.xpath("//h6[@class='oxd-text oxd-text--h6 oxd-topbar-header-breadcrumb-module']")).getText();
        System.out.println(dashboardText);
        Assert.assertTrue(dashboardText.equals("Cockpit") || dashboardText.equals("Dashboard"));
    }
}
