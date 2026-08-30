package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton-style Oracle DB connection helper.
 * Update URL / user / password to match your Oracle instance.
 */
public class DatabaseConnection {

    // === EDIT THESE FOR YOUR ENVIRONMENT ===
    private static final String URL      = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER     = "system";
    private static final String PASSWORD = "oracle";
    // ========================================

    private static Connection connection = null;

    private DatabaseConnection() { }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                connection.setAutoCommit(true);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Oracle JDBC driver not found. Add ojdbc8.jar to classpath.", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing DB connection: " + e.getMessage());
        }
    }
}
