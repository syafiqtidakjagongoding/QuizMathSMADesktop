/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import database.Database;
import entity.Kelas;
import entity.Leaderboard;
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
public class FinalScoreRepository {
    public List<Leaderboard> getAllScore(Component parentComponent) {
        String sql = """
            SELECT f.id,s.nama,k.kelas, s.nis,f.correct_answer,f.wrong_answer,f.total_question,f.final_score FROM final_score f INNER JOIN siswa s ON f.siswa_id = s.id INNER JOIN kelas k ON s.kelas_id = k.id ORDER BY f.final_score DESC
        """;
        List<Leaderboard> leaderboard = new ArrayList<>();
         try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String siswa = rs.getString("nama");
                String nis = rs.getString("nis");
                String kelas = rs.getString("kelas");
                int correct_answer = rs.getInt("correct_answer");
                int wrong_answer = rs.getInt("wrong_answer");
                int total_question = rs.getInt("total_question");
                int final_score = rs.getInt("final_score");
                
              Leaderboard lboard = new Leaderboard(id,siswa,nis,kelas, correct_answer,wrong_answer,total_question,final_score);
              leaderboard.add(lboard);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Gagal mengambil data final score: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        return leaderboard;
    }
}
