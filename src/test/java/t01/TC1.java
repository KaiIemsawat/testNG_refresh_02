package t01;

import org.testng.annotations.*;

public class TC1 {

    @BeforeSuite
    void beforeSuit() {
        System.out.println("THIS IS FROM BEFORE SUIT METHOD");
    }

    @AfterSuite
    void afterSuit() {
        System.out.println("THIS IS FROM AFTER SUIT METHOD");
    }

    @BeforeClass
    void beforeClass() {
        System.out.println("This is from - Before Class");
    }

    @AfterClass()
    void afterClass() {
        System.out.println("This is from - After Class");
    }

    @BeforeMethod
    void beforeMethod() {
        System.out.println("This is from Before Method");
    }

    @AfterMethod
    void afterMethod() {
        System.out.println("This is from After Method");
    }

    @Test
    void test1() {
        System.out.println("This is test 1");
    }

    @Test
    void test2() {
        System.out.println("This is test 2");
    }
}
