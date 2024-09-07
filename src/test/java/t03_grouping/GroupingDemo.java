package t03_grouping;

import org.testng.annotations.Test;

public class GroupingDemo {

    @Test(groups={"sanity"})
    void test1() {
        System.out.println("This is test ...01");
    }

    @Test(groups = {"smoke"})
    void test2() {
        System.out.println("This is test ...02");
    }

    @Test(groups = {"regression"})
    void test3() {
        System.out.println("This is test ...03");
    }

    @Test(groups = {"smoke"})
    void test4() {
        System.out.println("This is test ...04");
    }

    @Test(groups = {"smoke", "regression"})
    void test5() {
        System.out.println("This is test ...05");
    }
}
