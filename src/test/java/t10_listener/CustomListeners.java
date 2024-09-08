package t10_listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomListeners implements ITestListener {

    // This belongs to ITestListener and will execute before starting of Test set/batch
    public void onStart(ITestContext arg) {
        System.out.println("Start test execution..... " + arg.getName());
    }

    // This belongs to ITestListener and will execute after starting of Test set/batch
    public void onFinish(ITestContext arg) {
        System.out.println("Finish test execution..... " + arg.getName());
    }

    // This belongs to ITestListener and will execute before the main test start i.e. @Test
    public void onTestStart(ITestResult arg) {
        System.out.println("Start test..... " + arg.getName());
    }

    // This belongs to ITestListener and will execute when a test is skipped
    public void onTestSkipped(ITestResult arg) {
        System.out.println("Skipped test..... " + arg.getName());
    }

    // This belongs to ITestListener and will execute when a test is passed
    public void onTestSuccess(ITestResult arg) {
        System.out.println("Passed test..... " + arg.getName());
    }

    // This belongs to ITestListener and will execute when a test is failed
    public void onTestFailure(ITestResult arg) {
        System.out.println("Failed test..... " + arg.getName());
    }

    // This belongs to ITestListener and will execute when a test is failed
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg) {
        System.out.println("Failed test but success percentage..... " + arg.getName());
    }

}
