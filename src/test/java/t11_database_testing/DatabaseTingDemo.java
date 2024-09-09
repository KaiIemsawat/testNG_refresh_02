package t11_database_testing;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;


/*  pause part 6 at 14:52 */

public class DatabaseTingDemo {

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet;

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
        resultSet = statement.executeQuery("SHOW PROCEDURE STATUS WHERE name = 'SelectAllCustomers'");
        resultSet.next();

        Assert.assertEquals(resultSet.getString("Name"), "SelectAllCustomers");
    }
}
