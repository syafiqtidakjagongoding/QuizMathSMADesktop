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
    
    public List<DetailedAnswer> getStudentAnswers(int siswaId, Component parent) {
        List<DetailedAnswer> results = new ArrayList<>();
        
        System.out.println("DEBUG: Getting answers for siswaId: " + siswaId);
        
        try (Session session = Database.getSessionFactory().openSession()) {
            // Get all answers for this student with eager loading
            String hql = "FROM SiswaAnswer sa " +
                        "LEFT JOIN FETCH sa.optionAnswer oa " +
                        "LEFT JOIN FETCH oa.question q " +
                        "WHERE sa.siswa.id = :siswaId";
            List<SiswaAnswer> siswaAnswers = session.createQuery(hql, SiswaAnswer.class)
                    .setParameter("siswaId", siswaId)
                    .list();
            
            System.out.println("DEBUG: Found " + siswaAnswers.size() + " answers");
            
            int questionNumber = 1;
            for (SiswaAnswer sa : siswaAnswers) {
                OptionAnswer studentOption = sa.getOptionAnswer();
                Question question = studentOption.getQuestion();
                
                System.out.println("DEBUG: Processing question " + questionNumber + ": " + question.getQuestionText());
                
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
            
            System.out.println("DEBUG: Returning " + results.size() + " detailed answers");
            
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
        System.out.println("DEBUG: Getting siswa ID for finalScoreId: " + finalScoreId);
        
        try (Session session = Database.getSessionFactory().openSession()) {
            // Use explicit join to avoid lazy loading issues
            String hql = "SELECT s.id FROM FinalScore fs " +
                        "JOIN fs.siswa s " +
                        "WHERE fs.id = :id";
            Integer siswaId = session.createQuery(hql, Integer.class)
                    .setParameter("id", finalScoreId)
                    .uniqueResult();
            
            System.out.println("DEBUG: Found siswaId: " + siswaId);
            
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
}
