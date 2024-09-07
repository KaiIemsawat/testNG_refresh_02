package t02_dependencies;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DependencyDemo {

    @Test
    void startCar() {
        System.out.println("Start Car");
        Assert.fail();
    }

    @Test(dependsOnMethods = {"startCar"})
    void driveCar() {
        System.out.println("Drive Car");

    }

    @Test(dependsOnMethods = {"driveCar"})
    void stopCar() {
        System.out.println("Stop Car");
    }

    @Test(dependsOnMethods = {"driveCar", "stopCar"}, alwaysRun = true)
    void parkCar() {
        System.out.println("Park Car");
    }
}
