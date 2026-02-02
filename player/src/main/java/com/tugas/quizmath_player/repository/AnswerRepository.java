/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.database.Database;
import com.tugas.quizmath_player.entity.OptionAnswer;
import com.tugas.quizmath_player.entity.Siswa;
import com.tugas.quizmath_player.entity.SiswaAnswer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author syafiq
 */
public class AnswerRepository {

    public void upsertAnswer(int questionAnswerId, int siswaId) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // Logic: Ensure one answer per question for this student.
            // 1. Find the Question ID for the given OptionAnswer ID (questionAnswerId)
            OptionAnswer newOption = session.get(OptionAnswer.class, questionAnswerId);
            if (newOption == null)
                return; // Should not happen

            int questionId = newOption.getQuestion().getId();

            // 2. Find existing answer for this student and this QUESTION
            // We need to join SiswaAnswer -> OptionAnswer -> Question
            String hqlFind = "SELECT sa FROM SiswaAnswer sa WHERE sa.siswa.id = :sid AND sa.optionAnswer.question.id = :qid";
            SiswaAnswer existing = session.createQuery(hqlFind, SiswaAnswer.class)
                    .setParameter("sid", siswaId)
                    .setParameter("qid", questionId)
                    .uniqueResult();

            if (existing != null) {
                // Update
                existing.setOptionAnswer(newOption);
                session.merge(existing);
            } else {
                // Insert
                Siswa siswa = session.get(Siswa.class, siswaId);
                SiswaAnswer newSa = new SiswaAnswer(siswa, newOption);
                session.persist(newSa);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }
    }

    public void deleteAnswerByQuestion(int questionId, int siswaId) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String hqlDelete = "DELETE FROM SiswaAnswer sa WHERE sa.siswa.id = :sid AND sa.optionAnswer.question.id = :qid";
            session.createMutationQuery(hqlDelete)
                    .setParameter("sid", siswaId)
                    .setParameter("qid", questionId)
                    .executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        }
    }

    public List<String> getSelectedAnswers(int siswaId, int questionId) {
        try (Session session = Database.getSessionFactory().openSession()) {
            String hql = "SELECT sa.optionAnswer.label FROM SiswaAnswer sa WHERE sa.siswa.id = :sid AND sa.optionAnswer.question.id = :qid";
            return session.createQuery(hql, String.class)
                    .setParameter("sid", siswaId)
                    .setParameter("qid", questionId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
