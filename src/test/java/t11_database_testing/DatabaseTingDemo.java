package t11_database_testing;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class DatabaseTingDemo {

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet1 = null;
    ResultSet resultSet2 = null;
    CallableStatement callableStatement = null;

    /*
        Syntax
        {call procedure_name()}         Accept no parameter and return no value
        {call procedure_name(?,?}       Accept two parameters and return no value
        {? = call procedure_name()}     Accept no parameter and return value
        {? = call procedure_name(?)}    Accept one parameter and return value
    */

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

    @Test(priority = 1)
    void testStoredProceduresExists() throws SQLException {
        // uses with general queries
        statement = connection.createStatement();
        resultSet1 = statement.executeQuery("SHOW procedure status WHERE NAME = 'SelectAllCustomers'");
        resultSet1.next();

        Assert.assertEquals(resultSet1.getString("Name"), "SelectAllCustomers");
    }

    @Test(priority = 2)
    // {call procedure_name()}
    void testSelectAllCustomers() throws SQLException {
        // uses with store procedure
        callableStatement = connection.prepareCall("{CALL SelectAllCustomers()}");
        resultSet1 = callableStatement.executeQuery();

        Statement statement1 = connection.createStatement();
        resultSet2 = statement1.executeQuery("SELECT * FROM customers");

        Assert.assertTrue(compareResultSets(resultSet1, resultSet2));
    }

    @Test(priority = 3)
    // {call procedure_name(?}
    void testSelectAllCustomersByCity() throws SQLException {
        // uses with store procedure
        callableStatement = connection.prepareCall("{CALL SelectAllCustomersByCity(?)}");
        callableStatement.setString(1, "Singapore"); // parameter index starts at '1'
        resultSet1 = callableStatement.executeQuery();

        Statement statement1 = connection.createStatement();
        resultSet2 = statement1.executeQuery("SELECT * FROM customers WHERE city = 'Singapore'");

        Assert.assertTrue(compareResultSets(resultSet1, resultSet2));
    }

    @Test(priority = 3)
        // {call procedure_name(?,?}
    void testSelectAllCustomersByCityAndPostCode() throws SQLException {
        // uses with store procedure
        callableStatement = connection.prepareCall("{CALL SelectAllCustomersByCityAndPost(?, ?)}");
        callableStatement.setString(1, "Singapore"); // parameter index starts at '1'
        callableStatement.setString(2, "079903"); // parameter index starts at '1'

        resultSet1 = callableStatement.executeQuery();

        Statement statement1 = connection.createStatement();
        resultSet2 = statement1.executeQuery("SELECT * FROM customers WHERE city = 'Singapore' AND postalCode = '079903'");

        Assert.assertTrue(compareResultSets(resultSet1, resultSet2));
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
