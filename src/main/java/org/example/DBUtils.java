package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The class to connect to the database
 */
public class DBUtils {
    /** URL of database*/
    private static final String dbURL = "jdbc:postgresql://localhost:5432/Banks?useUnicode=yes&characterEncoding=UTF-8";

    /** the user's name of database*/
    private static final String dbUserName="postgres";
    /** the user's password of database */
    private static final String dbPassword="";


    /**
     * The method of connection to the database
     * @return returns the object of class Connection
     */
    public static Connection getConnection () {
        Connection connection=null;
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Тут не работает");
            }
            connection= DriverManager.getConnection(dbURL,dbUserName,dbPassword);
        } catch (SQLException e) {
            System.out.println("Что-то пошло не так....");
        }
        return connection;
    }
}
