package t11_database_testing;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTestingDemo01 {

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
    // Accept no parameter and return no value
    void testSelectAllCustomers() throws SQLException {
        // uses with store procedure
        callableStatement = connection.prepareCall("{CALL SelectAllCustomers()}");
        resultSet1 = callableStatement.executeQuery();

        Statement statement1 = connection.createStatement();
        resultSet2 = statement1.executeQuery("SELECT * FROM customers");

        Assert.assertTrue(compareResultSets(resultSet1, resultSet2));
    }

    @Test(priority = 3)
    // Accept one parameter and return no value
    void testSelectAllCustomersByCity() throws SQLException {
        // uses with store procedure
        callableStatement = connection.prepareCall("{CALL SelectAllCustomersByCity(?)}");
        callableStatement.setString(1, "Singapore"); // parameter index starts at '1'
        resultSet1 = callableStatement.executeQuery();

        Statement statement1 = connection.createStatement();
        resultSet2 = statement1.executeQuery("SELECT * FROM customers WHERE city = 'Singapore'");

        Assert.assertTrue(compareResultSets(resultSet1, resultSet2));
    }

    @Test(priority = 4)
        // Accept two parameters and return no value
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

    @Test(priority = 5)
    // Accept one parameter and return value
    void testGetOrderByCust() throws SQLException {
        callableStatement = connection.prepareCall("{CALL get_order_by_cust(?, ?, ?, ?, ?)}");

        // input
        callableStatement.setInt(1, 141);

        // output
        callableStatement.registerOutParameter(2, Types.INTEGER);
        callableStatement.registerOutParameter(3, Types.INTEGER);
        callableStatement.registerOutParameter(4, Types.INTEGER);
        callableStatement.registerOutParameter(5, Types.INTEGER);

        callableStatement.executeQuery();

        List<Integer> actual = new ArrayList<>();
        actual.add(callableStatement.getInt(2));
        actual.add(callableStatement.getInt(3));
        actual.add(callableStatement.getInt(4));
        actual.add(callableStatement.getInt(5));

        Statement statement1 = connection.createStatement();
        resultSet2 = statement1.executeQuery("SELECT (SELECT COUNT(*) AS 'shipped' FROM orders WHERE customerNumber = 141 AND status = 'Shipped') as Shipped, " +
                                    "(SELECT COUNT(*) AS 'canceled' FROM orders WHERE customerNumber = 141 AND status = 'Canceled') as Canceled, " +
                                    "(SELECT COUNT(*) AS 'resolved' FROM orders WHERE customerNumber = 141 AND status = 'Resolved') as Resolved, " +
                                    "(SELECT COUNT(*) AS 'disputed' FROM orders WHERE customerNumber = 141 AND status = 'Disputed') as Disputed");

        resultSet2.next();

        List<Integer> expected = new ArrayList<>();
        expected.add(resultSet2.getInt("shipped"));
        expected.add(resultSet2.getInt("canceled"));
        expected.add(resultSet2.getInt("resolved"));
        expected.add(resultSet2.getInt("disputed"));

        Assert.assertEquals(actual, expected);
    }

    @Test(priority = 6)
    void testGetCustomerShipping() throws SQLException {
        callableStatement = connection.prepareCall("{CALL GetCustomerShipping(?, ?)}");
        callableStatement.setInt(1, 112);

        callableStatement.registerOutParameter(2, Types.VARCHAR);

        callableStatement.executeQuery();

        String shippingTime = callableStatement.getString(2);

        Statement statement1 = connection.createStatement();
        resultSet2 = statement1.executeQuery("SELECT country, " +
                                                "CASE WHEN country = 'USA' THEN '2-day Shipping'" +
                                                "WHEN country = 'Canada' THEN '3-day Shipping' " +
                                                "ELSE '5-day Shipping' " +
                                                "END as ShippingTime " +
                                                "FROM customers " +
                                                "WHERE customerNumber=112");

        resultSet2.next();
        String expShippingTimme = resultSet2.getString("ShippingTime");

        Assert.assertEquals(shippingTime, expShippingTimme);
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
