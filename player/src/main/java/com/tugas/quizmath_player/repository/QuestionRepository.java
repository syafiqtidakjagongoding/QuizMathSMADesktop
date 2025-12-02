/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.entity.Answer;
import com.tugas.quizmath_player.entity.QuestionManipulation;
import com.tugas.quizmath_player.entity.QuestionId;
import com.tugas.quizmath_player.entity.Question;
import com.tugas.quizmath_player.entity.OptionAnswer;
import com.tugas.quizmath_player.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.awt.Component;
import java.sql.Statement;
import javax.swing.JOptionPane;
/**
 *
 * @author syafiq
 */
public class QuestionRepository {
     public QuestionId getAllId() {
        String sql = "SELECT id FROM question";
        List<Integer> ids = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }

            return new QuestionId(ids);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new QuestionId(new ArrayList<>()); // kalau error / kosong
    }
     
    public Question getQuestionById(int questionId) {
        String sql = "SELECT q.id, q.question_text, q.level, q.answer_type, " +
                     "q.topic, o.id AS id_answer, o.answer, o.correct, o.score, qi.image_path, o.image_answer " +
                     "FROM question q " +
                     "LEFT JOIN options_answer o ON o.question_id = q.id " +
                     "LEFT JOIN question_image qi ON qi.question_id = q.id " +
                     "WHERE q.id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, questionId);

            try (ResultSet rs = stmt.executeQuery()) {
                Question question = null;

                while (rs.next()) {
                    if (question == null) {
                        question = new Question(
                            rs.getInt("id"),
                            rs.getString("question_text"),
                            rs.getString("answer_type"), // answer_type belum ada di query
                            rs.getString("level"),
                            rs.getString("topic"),
                            rs.getString("image_path")
                        );
                    }

                    // Tambah opsi jawaban
                    int idAnswer = rs.getInt("id_answer");
                    String answer = rs.getString("answer");
                     int score = rs.getInt("score");
                     String image_answer = rs.getString("image_answer");
                    boolean correct = rs.getBoolean("correct");
                    question.addAnswer(new OptionAnswer(idAnswer, answer, score,correct, image_answer));
                }

                return question;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // kalau tidak ketemu
    }
    public List<QuestionManipulation> getAllQuestion(Component parentComponent) {
        List<QuestionManipulation> questions = new ArrayList<>();

        String sql = """
            SELECT q.id, q.question_text, q.answer_type, q.level, q.topic,
                   oa.id AS answer_id, oa.answer, oa.label, oa.score, oa.correct,
                   qi.image_path, oa.image_answer
            FROM question q
            INNER JOIN options_answer oa ON oa.question_id = q.id
            LEFT JOIN question_image qi ON qi.question_id = q.id
            ORDER BY q.id;
            """;

      try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        QuestionManipulation currentQuestion = null;
        int lastQuestionId = -1;

        while (rs.next()) {
            int qid = rs.getInt("id");

            // Kalau ketemu question baru
            if (qid != lastQuestionId) {
                currentQuestion = new QuestionManipulation(
                        qid,
                        rs.getString("question_text"),
                        rs.getString("image_path"),
                        rs.getString("answer_type"),
                        rs.getString("level"),
                        rs.getString("topic"),
                        new ArrayList<>() // bikin list kosong untuk jawaban
                );
                questions.add(currentQuestion);
                lastQuestionId = qid;
            }

            // Tambahkan jawaban ke question yang sedang aktif
            Answer ans = new Answer(
                    rs.getInt("answer_id"),
                    rs.getString("answer"),
                    rs.getString("label"),
                    rs.getInt("score"),
                    rs.getBoolean("correct"),
                    rs.getString("image_answer")
            );
            currentQuestion.answers.add(ans);
        }
    } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Gagal mengambil data soal: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
          return questions;

    }
   
  public void deleteSoal(int question_id, Component parentComponent) {
    String sql = "DELETE FROM question WHERE id = ?";

    try (Connection conn = Database.getConnection();  // ganti dengan method koneksi kamu
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, question_id);   // set parameter id

        int rowsDeleted = stmt.executeUpdate();
        if (rowsDeleted > 0) {
           JOptionPane.showMessageDialog(parentComponent, "Data soal berhasil dihapus!");
        } else {
           JOptionPane.showMessageDialog(parentComponent, "Data soal tidak ditemukan");

        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(parentComponent, "Gagal menghapus data soal: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

   
   public void createSoal(QuestionManipulation question,Component parentComponent) {
    String insertQuestion = """
        INSERT INTO question (question_text, answer_type, level, topic) 
        VALUES (?,?,?,?)
    """;

    String insertAnswer = """
        INSERT INTO options_answer (answer, question_id, score, correct, label, image_answer) 
        VALUES (?,?,?,?,?,?)
    """;

    String insertImage = """
        INSERT INTO question_image (image_path, question_id) 
        VALUES (?,?)
    """;

    try (Connection conn = Database.getConnection()) {
        conn.setAutoCommit(false);

        // 1. Insert ke question
        int questionId;
        try (PreparedStatement ps = conn.prepareStatement(insertQuestion, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, question.question_text);
            ps.setString(2, question.answer_type);
            ps.setString(3, question.level);
            ps.setString(4, question.topic);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    questionId = rs.getInt(1);
                } else {
                    throw new SQLException("Gagal ambil generated key untuk question");
                }
            }
        }

        // 2. Insert semua jawaban
        try (PreparedStatement ps = conn.prepareStatement(insertAnswer)) {
            for (Answer ans : question.answers) {
                ps.setString(1, ans.answer);
                ps.setInt(2, questionId);
                ps.setInt(3, ans.score);
                ps.setBoolean(4, ans.correct);
                ps.setString(5, ans.label);
                ps.setString(6, ans.image_answer);
                ps.addBatch();
            }
            ps.executeBatch();
        }

        // 3. Insert gambar kalau ada
        if (question.question_image != null && !question.question_image.isEmpty()) {
            try (PreparedStatement ps = conn.prepareStatement(insertImage)) {
                ps.setString(1, question.question_image);
                ps.setInt(2, questionId);
                ps.executeUpdate();
            }
        }

        conn.commit();
            JOptionPane.showMessageDialog(parentComponent, "Data soal baru berhasil disimpan!");

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(parentComponent, "Gagal menambah data soal: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
public void updateSoal(QuestionManipulation question, Component parentComponent) {
    String updateQuestion = """
        UPDATE question
        SET question_text = ?, answer_type = ?, level = ?, topic = ?
        WHERE id = ?
    """;

    String deleteAnswers = """
        DELETE FROM options_answer WHERE question_id = ?
    """;

    String insertAnswer = """
        INSERT INTO options_answer (answer, question_id, score, correct, label, image_answer)
        VALUES (?,?,?,?,?,?)
    """;

    String deleteImage = """
        DELETE FROM question_image WHERE question_id = ?
    """;

    String insertImage = """
        INSERT INTO question_image (image_path, question_id)
        VALUES (?,?)
    """;

    try (Connection conn = Database.getConnection()) {
        conn.setAutoCommit(false);

        // 1. Update tabel question
        try (PreparedStatement ps = conn.prepareStatement(updateQuestion)) {
            ps.setString(1, question.question_text);
            ps.setString(2, question.answer_type);
            ps.setString(3, question.level);
            ps.setString(4, question.topic);
            ps.setInt(5, question.id);   // <== PENTING: pastikan question.id sudah terisi
            ps.executeUpdate();
        }

        // 2. Hapus jawaban lama
        try (PreparedStatement ps = conn.prepareStatement(deleteAnswers)) {
            ps.setInt(1, question.id);
            ps.executeUpdate();
        }

        // 3. Insert jawaban baru
        try (PreparedStatement ps = conn.prepareStatement(insertAnswer)) {
            for (Answer ans : question.answers) {
                ps.setString(1, ans.answer);
                ps.setInt(2, question.id);
                ps.setInt(3, ans.score);
                ps.setBoolean(4, ans.correct);
                ps.setString(5, ans.label);
                ps.setString(6, ans.image_answer);
                ps.addBatch();
            }
            ps.executeBatch();
        }

        // 4. Update gambar
        //    (strategi: hapus semua, insert baru jika ada)
        try (PreparedStatement ps = conn.prepareStatement(deleteImage)) {
            ps.setInt(1, question.id);
            ps.executeUpdate();
        }

        if (question.question_image != null && !question.question_image.isEmpty()) {
            try (PreparedStatement ps = conn.prepareStatement(insertImage)) {
                ps.setString(1, question.question_image);
                ps.setInt(2, question.id);
                ps.executeUpdate();
            }
        }

        conn.commit();
        JOptionPane.showMessageDialog(parentComponent, "Data soal berhasil diperbarui!");

    } catch (SQLException e) {
        try {
            // rollback kalau error
            Database.getConnection().rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(parentComponent, "Gagal memperbarui data soal: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

}
