/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.repository;


import com.tugas.quizmath_player.entity.Kelas;
import com.tugas.quizmath_player.database.Database;

import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author syafiq
 */
public class KelasRepository {
    public List<Kelas> getAllKelas(Component parentComponent) {
        String sql = """
        SELECT id, kelas, jurusan FROM kelas
        """;
        List<Kelas> kelass = new ArrayList<>();
         try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String namaKelas = rs.getString("kelas");
                String jurusan = rs.getString("jurusan");
                
               Kelas kelas = new Kelas(id,namaKelas,jurusan);
               kelass.add(kelas);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Gagal mengambil data kelas: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        return kelass;
    }
}
