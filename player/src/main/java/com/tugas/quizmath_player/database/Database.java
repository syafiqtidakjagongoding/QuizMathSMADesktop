/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author syafiq
 */
public class Database {
    private static final String host = "localhost";
    private static final String port = "3306";
    private static final String dbname = "librequiz";
    private static final String username = "root";
    private static final String password = "root";
    
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Koneksi berhasil!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Gagal koneksi database: " + e.getMessage());
        }
       return conn;
    }
    
}
