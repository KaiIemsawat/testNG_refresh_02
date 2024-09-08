package t10_listener;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

//@Listeners(t10_listener.CustomListeners.class) // No need if use xml file
public class ListenerDemo {

    @Test
    void test01() {
        System.out.println("This is - Test01");
        Assert.assertEquals("A", "A");
    }

    @Test
    void test02() {
        System.out.println("This is - Test02");
        Assert.assertEquals("A", "B");
    }

    @Test
    void test03() {
        System.out.println("This is - Test03");
        throw new SkipException("This is a skip exception");
    }
}
