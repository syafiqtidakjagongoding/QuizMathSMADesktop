/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.entity.Leaderboard;

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
public class FinalScoreRepository {
    public void calculateScore(Component parentComponent, int siswaId) {
        String sqlBenar = """
                    SELECT COUNT(*) AS total_benar
                    FROM siswa_answer sa
                    INNER JOIN options_answer oa ON sa.question_answer_id = oa.id
                    WHERE oa.correct = 1 AND sa.siswa_id = ?
                """;

        String sqlTotalSoal = "SELECT COUNT(*) AS total_soal FROM question";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmtBenar = conn.prepareStatement(sqlBenar);
                PreparedStatement stmtTotal = conn.prepareStatement(sqlTotalSoal)) {

            // set siswaId untuk query jawaban benar
            stmtBenar.setInt(1, siswaId);

            int totalBenar = 0;
            try (ResultSet rsBenar = stmtBenar.executeQuery()) {
                if (rsBenar.next()) {
                    totalBenar = rsBenar.getInt("total_benar");
                }
            }

            int totalSoal = 0;
            try (ResultSet rsTotal = stmtTotal.executeQuery()) {
                if (rsTotal.next()) {
                    totalSoal = rsTotal.getInt("total_soal");
                }
            }

            if (totalSoal == 0) {
                JOptionPane.showMessageDialog(
                        parentComponent,
                        "Total soal masih 0, tidak bisa hitung skor.",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int totalSalah = totalSoal - totalBenar;
            double nilaiAkhir = ((double) totalBenar / totalSoal) * 100.0;

            String sqlInsertFinal = """
                        INSERT INTO final_score (siswa_id, correct_answer, wrong_answer, total_question, final_score)
                        VALUES (?, ?, ?, ?, ?)
                        ON DUPLICATE KEY UPDATE
                            correct_answer = VALUES(correct_answer),
                            wrong_answer = VALUES(wrong_answer),
                            total_question = VALUES(total_question),
                            final_score = VALUES(final_score)
                    """;

            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsertFinal)) {
                stmtInsert.setInt(1, siswaId);
                stmtInsert.setInt(2, totalBenar);
                stmtInsert.setInt(3, totalSalah);
                stmtInsert.setInt(4, totalSoal);
                stmtInsert.setDouble(5, nilaiAkhir);
                stmtInsert.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Soal sudah terkirim",
                    "Soal terkirim",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi kesalahan saat menghitung skor:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void insertScore(Component parentComponent, int siswaId, int correctAnswer, int totalQuestion) {

        int wrongAnswer = totalQuestion - correctAnswer;
        double finalScore = ((double) correctAnswer / totalQuestion) * 100.0;

        String sqlInsertFinal = """
                INSERT INTO final_score (siswa_id, correct_answer, wrong_answer, total_question, final_score)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    correct_answer = VALUES(correct_answer),
                    wrong_answer = VALUES(wrong_answer),
                    total_question = VALUES(total_question),
                    final_score = VALUES(final_score)
                """;

        try (Connection conn = Database.getConnection();
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsertFinal)) {

            stmtInsert.setInt(1, siswaId);
            stmtInsert.setInt(2, correctAnswer);
            stmtInsert.setInt(3, wrongAnswer);
            stmtInsert.setInt(4, totalQuestion);
            stmtInsert.setDouble(5, finalScore);

            stmtInsert.executeUpdate();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Soal sudah terkirim!",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi kesalahan saat menyimpan skor:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

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

                Leaderboard lboard = new Leaderboard(id, siswa, nis, kelas, correct_answer, wrong_answer,
                        total_question,
                        final_score);
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
