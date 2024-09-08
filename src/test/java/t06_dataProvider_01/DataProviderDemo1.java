package t06_dataProvider_01;

import org.testng.annotations.DataProvider;

public class DataProviderDemo1 {

    @DataProvider(name = "CredentialDataProvider")
    public Object[][] getData() {
        Object[][] data = {
                {"abc@email.com", "abc123"}
                , {"def@email.com", "def123"}
                , {"ghi@email.com", "ghi123"}
        };
        return data;
    }
}
