/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author datth
 */
public class DatabaseConnection {
    // table account
    // db 
    private static final String URL = "jdbc:postgresql://localhost:5432/java_card";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456";
    private static Connection connection = null;
    public static Connection connect() {
        try {
            // Load the PostgreSQL JDBC driver (optional in newer versions)
            Class.forName("org.postgresql.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Connection to the database was successful!");
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
        return connection;
    }
    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from the database.");
            } catch (SQLException e) {
                System.out.println("Error while disconnecting from the database.");
                e.printStackTrace();
            }
        }
    }
}
