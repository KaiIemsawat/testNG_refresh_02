package t11_database_testing;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;


/*  pause part 6 at 14:52 */

public class DatabaseTingDemo {

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet01;
    ResultSet resultSet02;
    ResultSet resultSet03;
    CallableStatement callableStatement;

    @BeforeClass
    void setup() {
        try {
            // Optionally load the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Correct JDBC URL
            /* NOTE : IT COULD BE 'jdbc:mysql://127.0.0.1:3306/' OR 'jdbc:mysql://localhost:3306/' - NEED TO CHOOSE THE CORRECT ONE*/
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "rootroot");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Handle class loading error if driver isn't found
        }
    }


    @AfterClass
    void teardown() throws SQLException {
        connection.close();
    }

    @Test(priority = 1)
    void test_storeProceduresExists() throws SQLException {
        statement = connection.createStatement();
        // We can use 'executeQuery' with normal statement.
        // But for STORE PROCEDURE we need 'callableStatement' as below
        resultSet01 = statement.executeQuery("SHOW PROCEDURE STATUS WHERE name = 'SelectAllCustomers'");
        resultSet01.next();

        Assert.assertEquals(resultSet01.getString("Name"), "SelectAllCustomers");
    }

    @Test(priority = 2)
    void test_selectAllCustomers() throws SQLException {
        callableStatement =  connection.prepareCall("{CALL SelectAllCustomers()}");
        resultSet02 = callableStatement.executeQuery();

        statement = connection.createStatement();
        resultSet03 = statement.executeQuery("SELECT * FROM customers");
        Assert.assertTrue(compareResultSets(resultSet02, resultSet03));
    }

    @Test(priority = 3)
    void test_selectAllCustomersByCity() throws SQLException {
        // using 'prepareCall' with parameter
        callableStatement =  connection.prepareCall("{CALL SelectAllCustomersByCity(?)}");
        callableStatement.setString(1, "Singapore");
        resultSet02 = callableStatement.executeQuery();

        statement = connection.createStatement();
        resultSet03 = statement.executeQuery("SELECT * FROM customers WHERE city = 'Singapore'");
        Assert.assertTrue(compareResultSets(resultSet02, resultSet03));
    }

    @Test(priority = 4)
    void test_selectAllCustomersByCityAndPinCode() throws SQLException {
        // using 'prepareCall' with multiple parameters
        callableStatement =  connection.prepareCall("{CALL SelectAllCustomersByCityAndPin(?, ?)}");
        callableStatement.setString(1, "Singapore");
        callableStatement.setString(2, "079903");

        resultSet02 = callableStatement.executeQuery();

        statement = connection.createStatement();
        resultSet03 = statement.executeQuery("SELECT * FROM customers WHERE city = 'Singapore' AND postalCode = '079903'");
        Assert.assertTrue(compareResultSets(resultSet02, resultSet03));
    }

    @Test(priority = 5)
    void test_getOrderByCust() throws SQLException {
        // Need to include both expected return and input parameters
        callableStatement = connection.prepareCall("{CALL get_order_by_cust(?, ?, ?, ?, ?)}");
        // set input parameter -- Note : parameter index starts at '1'
        callableStatement.setInt(1, 141);
        // set output parameters, positions and types -- Note : parameter index starts at '1'
        callableStatement.registerOutParameter(2, Types.INTEGER);
        callableStatement.registerOutParameter(3, Types.INTEGER);
        callableStatement.registerOutParameter(4, Types.INTEGER);
        callableStatement.registerOutParameter(5, Types.INTEGER);

        callableStatement.executeQuery();
        // -- Note : parameter index starts at '1'
        int shipped = callableStatement.getInt(2);
        int canceled = callableStatement.getInt(3);
        int resolved = callableStatement.getInt(4);
        int disputed = callableStatement.getInt(5);

        // System.out.println(shipped + " " + canceled + " " + resolved + " " + disputed);

        statement = connection.createStatement();
        resultSet01 = statement.executeQuery("SELECT \n" +
                "    (SELECT count(*) as 'shipped' FROM orders WHERE customerNumber = 141 AND status = 'Shipped') as Shipped,\n" +
                "    (SELECT count(*) as 'canceled' FROM orders WHERE customerNumber = 141 AND status = 'Canceled') as Canceled,\n" +
                "    (SELECT count(*) as 'resolved' FROM orders WHERE customerNumber = 141 AND status = 'Resolved') as Resolved,\n" +
                "    (SELECT count(*) as 'disputed' FROM orders WHERE customerNumber = 141 AND status = 'Disputed') as Disputed;");

        resultSet01.next();

        // get values from each column
        int expShipped = resultSet01.getInt("shipped");
        int expCanceled = resultSet01.getInt("canceled");
        int expResolved = resultSet01.getInt("resolved");
        int expDisputed = resultSet01.getInt("disputed");

        if (shipped == expShipped && canceled == expCanceled && resolved == expResolved && disputed == expDisputed) {
            Assert.assertTrue(true);
        } else Assert.assertTrue(false);
    }

    @Test(priority = 6)
    void test_getCustomerShipping() throws SQLException {
        // Need to include both expected return and input parameters
        callableStatement = connection.prepareCall("{CALL GetCustomerShipping(?, ?)}");
        // set input parameter -- Note : parameter index starts at '1'
        callableStatement.setInt(1, 112);
        // set output parameters, positions and types -- Note : parameter index starts at '1'
        callableStatement.registerOutParameter(2, Types.VARCHAR);

        callableStatement.executeQuery();
        // -- Note : parameter index starts at '1'
        String shippingTime  = callableStatement.getString(2);

        statement = connection.createStatement();
        resultSet01 = statement.executeQuery(
                "SELECT country, CASE WHEN country = 'USA' THEN '2-day Shipping' \n" +
                "WHEN country='Canada' THEN '3-day Shipping' \n" +
                "ELSE '5-day Shipping' END as ShippingTime \n" +
                "FROM customers WHERE customerNumber = 112"
                );

        resultSet01.next();

        String expectedShippingTime = resultSet01.getString("shippingTime");

        Assert.assertEquals(shippingTime, expectedShippingTime);

    }

    //    Method to compare resultSet
    private boolean compareResultSets(ResultSet rs1, ResultSet rs2) throws SQLException {
        while (rs1.next()) {
            rs2.next();
            int count = rs1.getMetaData().getColumnCount();
            for (int i = 1; i<= count; i++) {
                // Import StringUtils from 'org.apache.commons.lang3.StringUtils;'
                if (!StringUtils.equals(rs1.getString(i), rs2.getString(i))) {
                    return false;
                }
            }
        }
        return true;
    }


}
