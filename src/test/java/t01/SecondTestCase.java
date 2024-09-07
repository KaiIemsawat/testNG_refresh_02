package t01;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SecondTestCase {

    @Test
    void setup() {
        System.out.println("Second Setup");
    }

    @Test(priority = 2)
    void searchCustomer() {
        System.out.println("search customer");
    }

    @Test(priority = 1)
    void addCustomer() {
        System.out.println("add customer");
    }

    @Test(priority = 3)
    void teardown() {
        System.out.println("2nd Teardown");
    }
}
