package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static String dbURL = "jdbc:postgresql://localhost:5432/Banks";

    private static String dbUserName="postgres";
    private static String dbPassword="";


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
