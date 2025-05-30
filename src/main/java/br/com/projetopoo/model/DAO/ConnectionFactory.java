package br.com.projetopoo.model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/projetopoo"; 
    private static final String USER = "postgres"; 
    private static final String PASS = "123456"; 

    public static Connection getConnection() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) { 
            return conn; 
        } 
        catch (SQLException e) { 
            return null;
        } 
    }
}
