package t01;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class TC2 {

    @BeforeTest
    void beforeTest() {
        System.out.println("This is from == Before Test");
    }

    @AfterTest
    void afterTest() {
        System.out.println("This is from == After Test");
    }
}
