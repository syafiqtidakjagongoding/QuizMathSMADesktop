/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.database.Database;
import com.tugas.quizmath_player.entity.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author syafiq
 */
public class AdminRepository {
    public boolean checkLogin(Admin admin) {
        String sql = "SELECT * FROM admin WHERE user = ? AND password = ?";
         try (Connection conn = Database.getConnection();  // ganti sesuai class koneksi kamu
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, admin.getUsername());
        stmt.setString(2, admin.getPassword());

        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next(); // kalau ada data → login valid
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }
}
