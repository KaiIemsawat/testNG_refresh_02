package t12_database_testing_02;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class DatabaseTestingDemo02 {

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet1 = null;
    ResultSet resultSet2 = null;
    CallableStatement callableStatement = null;


    @BeforeClass
    void setup() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "rootroot");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @AfterClass
    void teardown() {
        try {
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Test
    void  testStoredFunctionExist() throws SQLException {
        statement = connection.createStatement();
        resultSet1 = statement.executeQuery("SHOW FUNCTION STATUS WHERE Name = 'CustomerLevel'");
        resultSet1.next();

        Assert.assertEquals(resultSet1.getString("Name"), "CustomerLevel");
    }

    @Test
    void  testCustomerWithSqlStatement() throws SQLException {
        statement = connection.createStatement();
        resultSet1 = statement.executeQuery("SELECT customerName, CustomerLevel(creditLimit) FROM customers");
        resultSet2 = statement.executeQuery("SELECT customerName, " +
                "CASE WHEN creditLimit > 50000 THEN 'PLATINUM' " +
                "WHEN creditLimit >= 10000 AND creditLimit <= 50000 THEN 'GOLD' " +
                "WHEN creditLimit < 10000 THEN 'SILVER' " +
                "END AS customerLevel " +
                "FROM customers");

        Assert.assertTrue(compareResultSets(resultSet1, resultSet2));
    }

    @Test
    void tesCustomerLevelWithStoredProcedure() throws SQLException {
        callableStatement = connection.prepareCall("{CALL GetCustomerLevel(?, ?)}");

        callableStatement.setInt(1, 141);
        callableStatement.registerOutParameter(2, Types.VARCHAR);

        callableStatement.executeQuery();

        String customerLevel = callableStatement.getString(2);

        resultSet1 = connection.createStatement().executeQuery("SELECT customerName, " +
                "CASE WHEN creditLimit > 50000 THEN 'PLATINUM' " +
                "WHEN creditLimit >= 10000 AND creditLimit <= 50000 THEN 'GOLD' " +
                "WHEN creditLimit < 10000 THEN 'SILVER' " +
                "END AS customerLevel " +
                "FROM customers " +
                "WHERE CustomerNumber=141");

        resultSet1.next();

        String expectedCustomerLevel = resultSet1.getString("customerLevel");

        Assert.assertEquals(customerLevel, expectedCustomerLevel);
    }

    private boolean compareResultSets(ResultSet resultSet1, ResultSet resultSet2) {
        try {
            while (resultSet1.next()) {
                resultSet2.next();
                int count = resultSet1.getMetaData().getColumnCount();
                for (int i = 1; i<=count; i++) {
                    if (!StringUtils.equals(resultSet1.getString(i), resultSet2.getString(i))) {
                        return false;
                    }
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return true;
    }
}
