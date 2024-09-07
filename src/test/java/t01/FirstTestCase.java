package t01;

import org.testng.annotations.Test;

public class FirstTestCase {

    @Test
    void setup() {
        System.out.println("Setup");
    }

    @Test(priority = 1)
    void login() {
        System.out.println("Login");
    }

    @Test(priority = 2)
    void teardown() {
        System.out.println("Teardown");
    }
}
