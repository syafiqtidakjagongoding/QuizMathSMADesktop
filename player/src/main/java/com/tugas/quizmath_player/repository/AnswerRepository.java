/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import database.Database;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author syafiq
 */
public class AnswerRepository {
   public void upsertAnswer(int questionAnswerId, int siswaId) {
    String sql = "INSERT INTO siswa_answer (question_answer_id, siswa_id) " +
                 "VALUES (?, ?) " +
                 "ON DUPLICATE KEY UPDATE question_answer_id = VALUES(question_answer_id)";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, questionAnswerId);
        stmt.setInt(2, siswaId);

        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
   public void deleteAnswerByQuestion(int questionId, int siswaId) {
    String sql = "DELETE FROM siswa_answer " +
                 "WHERE siswa_id = ? AND question_answer_id IN (" +
                 "   SELECT id FROM options_answer WHERE question_id = ?" +
                 ")";
    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, siswaId);
        stmt.setInt(2, questionId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public List<String> getSelectedAnswers(int siswaId, int questionId) {
    List<String> selected = new ArrayList<>();

    String sql = "SELECT oa.label FROM siswa_answer s INNER JOIN options_answer oa ON s.question_answer_id = oa.id WHERE s.siswa_id = ? AND oa.question_id = ?";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, siswaId);
        stmt.setInt(2, questionId);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            selected.add(rs.getString("label")); // A/B/C/D
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return selected;
}

}
