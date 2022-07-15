
package IndividualProjectStuff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Ederi_Kobi_IP_Task7 {

    private Connection conn;

    // Azure SQL connection credentials
    private String server = "eder0000-sql-server.database.windows.net";
    private String database = "cs-dsa-4513-sql-db";
    private String username = "eder0000";
    private String password = "Thinkdifferent#4";

    // Resulting connection string
    final private String url =
            String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
                    server, database, username, password);

    // Initialize and save the database connection
    private void getDBConnection() throws SQLException {
        if (conn != null) {
            return;
        }

        this.conn = DriverManager.getConnection(url);
    }

    // Return the result of selecting everything from the movie_night table 
    public ResultSet getAllCustomers() throws SQLException {
        getDBConnection();
        
        final String sqlQuery = "SELECT * FROM Customer;";
        final PreparedStatement stmt = conn.prepareStatement(sqlQuery);
        return stmt.executeQuery();
    }

    // Inserts a record into the movie_night table with the given attribute values
    public boolean addCustomer(String custName, String custAddress, int category) throws SQLException {

        getDBConnection(); // Prepare the database connection

        // Prepare the SQL statement
        final String sqlQuery =
                "INSERT INTO Customer " + 
                    "(cust_name, cust_address, cust_category) " + 
                "VALUES " +  "(?, ?, ?)";
        final PreparedStatement stmt = conn.prepareStatement(sqlQuery);

        // Replace the '?' in the above statement with the given attribute values
        stmt.setString(1, custName);
        stmt.setString(2, custAddress);
        stmt.setInt(3, category);

        // Execute the query, if only one record is updated, then we indicate success by returning true
        return stmt.executeUpdate() == 1;
    }
    
    // Return the result of selecting everything from the movie_night table 
    public ResultSet getCustInRange(int min, int max) throws SQLException {
        getDBConnection();
        
        final String sqlQuery = "SELECT cust_name FROM Customer WHERE cust_category BETWEEN ? AND ?;";
        final PreparedStatement stmt = conn.prepareStatement(sqlQuery);
        
        stmt.setInt(1, min);
        stmt.setInt(2, max);
        
        return stmt.executeQuery();
    }
    
    
}
