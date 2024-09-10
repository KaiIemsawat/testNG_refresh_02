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
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/classicmodels", "root", "rootroot");

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
