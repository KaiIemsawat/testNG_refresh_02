package t06_dataProvider_01;

import org.testng.annotations.Test;

public class DataProvider01 {

    @Test(dataProvider = "CredentialDataProvider", dataProviderClass = DataProviderDemo1.class)
    public void loginTest(String email, String password) {
        System.out.println(email + " - " + password);
    }

}
