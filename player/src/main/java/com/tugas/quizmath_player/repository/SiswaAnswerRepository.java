package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.database.Database;
import com.tugas.quizmath_player.entity.DetailedAnswer;
import com.tugas.quizmath_player.entity.OptionAnswer;
import com.tugas.quizmath_player.entity.Question;
import com.tugas.quizmath_player.entity.SiswaAnswer;
import org.hibernate.Session;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class SiswaAnswerRepository {
    
    public List<DetailedAnswer> getStudentAnswers(int finalScoreId, Component parent) {
        List<DetailedAnswer> results = new ArrayList<>();
        
        
        try (Session session = Database.getSessionFactory().openSession()) {
            // Get all answers for this score with eager loading
            String hql = "FROM SiswaAnswer sa " +
                        "LEFT JOIN FETCH sa.optionAnswer oa " +
                        "LEFT JOIN FETCH oa.question q " +
                    "WHERE sa.finalScore.id = :finalScoreId";
            List<SiswaAnswer> siswaAnswers = session.createQuery(hql, SiswaAnswer.class)
                    .setParameter("finalScoreId", finalScoreId)
                    .list();
            
            
            int questionNumber = 1;
            for (SiswaAnswer sa : siswaAnswers) {
                OptionAnswer studentOption = sa.getOptionAnswer();
                Question question = studentOption.getQuestion();
                
                
                // Get the correct answer for this question
                String correctAnswerText = "";
                for (OptionAnswer opt : question.getAnswers()) {
                    if (opt.isCorrect()) {
                        correctAnswerText = opt.getLabel() + ". " + opt.getAnswer();
                        break;
                    }
                }
                
                DetailedAnswer detail = new DetailedAnswer(
                    questionNumber++,
                    question.getQuestionText(),
                    studentOption.getLabel() + ". " + studentOption.getAnswer(),
                    studentOption.isCorrect(),
                    correctAnswerText,
                    question.getId()
                );
                
                results.add(detail);
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR in getStudentAnswers: " + e.getMessage());
            JOptionPane.showMessageDialog(parent, 
                "Error loading student answers: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return results;
    }
    
    public int getStudentIdByFinalScoreId(int finalScoreId, Component parent) {
        
        try (Session session = Database.getSessionFactory().openSession()) {
            // Use explicit join to avoid lazy loading issues
            String hql = "SELECT s.id FROM FinalScore fs " +
                        "JOIN fs.siswa s " +
                        "WHERE fs.id = :id";
            Integer siswaId = session.createQuery(hql, Integer.class)
                    .setParameter("id", finalScoreId)
                    .uniqueResult();
            
            
            return siswaId != null ? siswaId : -1;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR in getStudentIdByFinalScoreId: " + e.getMessage());
            JOptionPane.showMessageDialog(parent, 
                "Error getting student ID: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    public void saveStudentAnswers(List<SiswaAnswer> answers, Component parent) {
        org.hibernate.Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            for (SiswaAnswer sa : answers) {
                String sql = "INSERT INTO siswa_answer (siswa_id, question_answer_id, final_score_id) VALUES (:siswaId, :optionId, :finalScoreId)";
                session.createNativeQuery(sql)
                        .setParameter("siswaId", sa.getSiswa().getId())
                        .setParameter("optionId", sa.getOptionAnswer().getId())
                        .setParameter("finalScoreId", sa.getFinalScore().getId())
                        .executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            System.err.println("ERROR in saveStudentAnswers: " + e.getMessage());
            JOptionPane.showMessageDialog(parent,
                    "Error saving student answers: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
