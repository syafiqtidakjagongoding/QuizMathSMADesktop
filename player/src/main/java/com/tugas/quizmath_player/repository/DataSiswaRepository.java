/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.entity.DataSiswa;
import com.tugas.quizmath_player.entity.Siswa;
import com.tugas.quizmath_player.database.Database;

import com.tugas.quizmath_player.entity.Kelas;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.tugas.quizmath_player.helper.Session;
import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author syafiq
 */
public class DataSiswaRepository {

    public DataSiswa loginSiswa(DataSiswa dataSiswa, Component rootPane) {
        String sqlQuery = "SELECT s.id, s.username, s.nama, s.nis, s.no_absen, k.kelas, k.jurusan FROM siswa s INNER JOIN kelas k ON s.kelas_id = k.id WHERE s.username = ? AND s.password = ?";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setString(1, dataSiswa.username);
            stmt.setString(2, dataSiswa.password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int siswaId = rs.getInt("id");
                    String name = rs.getString("nama");
                    String username = rs.getString("username");
                    String nis = rs.getString("nis");
                    int absen = rs.getInt("no_absen");
                    String kelas = rs.getString("kelas");
                    String jurusan = rs.getString("jurusan");

                    String absenStr = String.valueOf(absen);
                    System.out.println("✅ Login berhasil: " + name);

                    // langsung set session
                    Session.setSession(siswaId, name);
                    return new DataSiswa(username, absenStr, name, nis, kelas, jurusan);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "⚠️ Username atau password salah");
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal login siswa: " + e.getMessage());
        }
        return null;
    }

    public DataSiswa getSiswaById(int id, Component rootPane) {
        String sqlQuery = "SELECT  s.username, s.no_absen, s.nama, s.nis, k.kelas " +
                "FROM siswa s " +
                "INNER JOIN kelas k ON s.kelas_id = k.id " +
                "WHERE s.id = ?";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Ambil data dari ResultSet
                    String username = rs.getString("username");
                    String absen = rs.getString("no_absen");
                    String name = rs.getString("nama");
                    String nis = rs.getString("nis");
                    String kelas = rs.getString("kelas");

                    // Buat objek DataSiswa
                    return new DataSiswa(username, absen, name, nis, kelas, "");
                } else {
                    JOptionPane.showMessageDialog(rootPane, "⚠️ Siswa dengan ID " + id + " tidak ditemukan");
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal mengambil data siswa: " + e.getMessage());
            return null;
        }
    }

    public List<Kelas> getAllKelas() {
        List<Kelas> listKelas = new ArrayList<>();
        String sql = "SELECT * FROM kelas";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String kelas = rs.getString("kelas");
                String jurusan = rs.getString("jurusan");

                Kelas k = new Kelas(id, kelas, jurusan);
                listKelas.add(k);
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal ambil data kelas: " + e.getMessage());
        }

        return listKelas;
    }

    public List<Siswa> getAllSiswa(Component parentComponent) {
        List<Siswa> result = new ArrayList<>();
        String sql = """
                    SELECT s.id, s.nama,s.username,s.password, s.no_absen, s.nis, k.kelas,k.jurusan
                    FROM siswa s
                    INNER JOIN kelas k ON s.kelas_id = k.id
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String kelas = rs.getString("kelas");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String jurusan = rs.getString("jurusan");
                String nis = rs.getString("nis");
                int noAbsen = rs.getInt("no_absen");

                Siswa siswa = new Siswa(id, nama, username, password, nis, noAbsen, kelas, jurusan);
                result.add(siswa);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi error saat mengambil data siswa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return result;
    }

    public void deleteSiswa(int selectedId, Component parentComponent) {
        String deleteFinalScore = "DELETE FROM final_score WHERE siswa_id = ?";
        String deleteSiswa = "DELETE FROM siswa WHERE id = ?";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false); // mulai transaksi

            // 1. Hapus final_score siswa
            try (PreparedStatement stmtFinal = conn.prepareStatement(deleteFinalScore)) {
                stmtFinal.setInt(1, selectedId);
                stmtFinal.executeUpdate();
            }

            // 2. Hapus data siswa
            try (PreparedStatement stmtSiswa = conn.prepareStatement(deleteSiswa)) {
                stmtSiswa.setInt(1, selectedId);
                stmtSiswa.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Data siswa dan skor final berhasil dihapus.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi error saat menghapus data siswa: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void updateSiswa(Siswa siswa, int kelas_id, Component parentComponent) {
        String sql = """
                    UPDATE siswa
                    SET nama = ?, no_absen = ?, nis = ?, kelas_id = ?, username = ?, password = ?
                    WHERE id = ?
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, siswa.nama);
            stmt.setInt(2, siswa.no_absen);
            stmt.setString(3, siswa.nis);
            stmt.setInt(4, kelas_id);
            stmt.setString(5, siswa.username);
            stmt.setString(6, siswa.password);
            stmt.setInt(7, siswa.id);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Data siswa berhasil diedit.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi error saat mengedit data siswa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
     public void buatSiswa(Siswa siswa, int kelas_id, Component parentComponent) {
        String sql = """
                     INSERT INTO siswa (nama, kelas_id,username,password,nis,no_absen) 
                     VALUES (?,?,?,?,?,?)
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, siswa.nama);
             stmt.setInt(2, kelas_id);
              stmt.setString(3, siswa.username);
            stmt.setString(4, siswa.password);
             stmt.setString(5, siswa.nis);
            stmt.setInt(6, siswa.no_absen);
           
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Data siswa berhasil dibuat.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi error saat membuat data siswa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
