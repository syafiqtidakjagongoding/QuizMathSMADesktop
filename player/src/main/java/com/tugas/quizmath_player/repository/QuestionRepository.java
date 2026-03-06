/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.entity.*;
import com.tugas.quizmath_player.database.Database;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author syafiq
 */
public class QuestionRepository {

    public QuestionId getAllId() {
        try (Session session = Database.getSessionFactory().openSession()) {
            List<Integer> ids = session.createQuery("SELECT q.id FROM Question q", Integer.class).list();
            return new QuestionId(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new QuestionId(new ArrayList<>());
        }
    }

    public Question getQuestionById(int questionId) {
        try (Session session = Database.getSessionFactory().openSession()) {
            Question question = session.get(Question.class, questionId);
            if (question != null) {
                // Fetch image manually as it is not mapped in Question entity directly
                // (transient field)
                String hqlImage = "SELECT qi.imagePath FROM QuestionImage qi WHERE qi.question.id = :qid";
                String imagePath = session.createQuery(hqlImage, String.class)
                        .setParameter("qid", questionId)
                        .uniqueResult();
                question.setImagePath(imagePath);
                return question;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<QuestionManipulation> getAllQuestion(Component parentComponent) {
        List<QuestionManipulation> result = new ArrayList<>();
        try (Session session = Database.getSessionFactory().openSession()) {
            List<Question> questions = session.createQuery("FROM Question", Question.class).list();

            for (Question q : questions) {
                // Fetch image
                String hqlImage = "SELECT qi.imagePath FROM QuestionImage qi WHERE qi.question.id = :qid";
                String imagePath = session.createQuery(hqlImage, String.class)
                        .setParameter("qid", q.getId())
                        .uniqueResult();

                List<Answer> answers = new ArrayList<>();
                for (OptionAnswer oa : q.getAnswers()) {
                    answers.add(new Answer(
                            oa.getId(),
                            oa.getAnswer(),
                            oa.getLabel(),
                            oa.getScore(),
                            oa.isCorrect()));
                }

                QuestionManipulation qm = new QuestionManipulation(
                        q.getId(),
                        q.getQuestionText(),
                        imagePath,
                        q.getAnswerType(),
                        q.getLevel(),
                        q.getTopic(),
                        answers);
                result.add(qm);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Gagal mengambil data soal: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    public void deleteSoal(int question_id, Component parentComponent) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Question q = session.get(Question.class, question_id);
            if (q != null) {
                // Delete image manual if not cascaded (not mapped in Question)
                session.createMutationQuery("DELETE FROM QuestionImage WHERE question.id = :qid")
                        .setParameter("qid", question_id)
                        .executeUpdate();

                session.remove(q);
                tx.commit();
                JOptionPane.showMessageDialog(parentComponent, "Data soal berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(parentComponent, "Data soal tidak ditemukan");
            }
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            JOptionPane.showMessageDialog(parentComponent, "Gagal menghapus data soal: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void createSoal(QuestionManipulation questionDto, Component parentComponent) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Question q = new Question();
            q.setQuestionText(questionDto.question_text);
            q.setAnswerType(questionDto.answer_type);
            q.setLevel(questionDto.level);
            q.setTopic(questionDto.topic);

            // Save question first to get ID
            session.persist(q);

            // Save image if exists
            if (questionDto.question_image != null && !questionDto.question_image.isEmpty()) {
                QuestionImage qi = new QuestionImage(questionDto.question_image, q);
                session.persist(qi);
            }

            // Save answers
            for (Answer ans : questionDto.answers) {
                OptionAnswer oa = new OptionAnswer();
                oa.setAnswer(ans.answer);
                oa.setScore(ans.score);
                oa.setCorrect(ans.correct);
                oa.setLabel(ans.label);
                oa.setQuestion(q);
                session.persist(oa);
            }

            tx.commit();
            JOptionPane.showMessageDialog(parentComponent, "Data soal baru berhasil disimpan!");
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Gagal menambah data soal: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateSoal(QuestionManipulation questionDto, Component parentComponent) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Question q = session.get(Question.class, questionDto.id);
            if (q != null) {
                q.setQuestionText(questionDto.question_text);
                q.setAnswerType(questionDto.answer_type);
                q.setLevel(questionDto.level);
                q.setTopic(questionDto.topic);

                // Update Image
                // Delete old image
                session.createMutationQuery("DELETE FROM QuestionImage WHERE question.id = :qid")
                        .setParameter("qid", q.getId())
                        .executeUpdate();

                // Insert new if exists
                if (questionDto.question_image != null && !questionDto.question_image.isEmpty()) {
                    QuestionImage qi = new QuestionImage(questionDto.question_image, q);
                    session.persist(qi);
                }

                // Update Answers: Delete old, add new (simplest strategy as in original)
                // Or we could merge, but original code deleted all.
                // We can use orphanRemoval if mapped, but here we can just clear list or
                // manually delete.
                // Since we have CascadeType.ALL on answers list in Question entity, we can just
                // clear the list and add new ones if we manage the collection.
                // However, simpler to match original logic: delete all options for this
                // question.
                session.createMutationQuery("DELETE FROM OptionAnswer WHERE question.id = :qid")
                        .setParameter("qid", q.getId())
                        .executeUpdate();
                q.getAnswers().clear();
                session.flush();

                for (Answer ans : questionDto.answers) {
                    OptionAnswer oa = new OptionAnswer();
                    oa.setAnswer(ans.answer);
                    oa.setScore(ans.score);
                    oa.setCorrect(ans.correct);
                    oa.setLabel(ans.label);

                    q.addAnswer(oa); // managed by cascade
                    session.persist(oa);
                }
                session.merge(q);
            }

            tx.commit();
            JOptionPane.showMessageDialog(parentComponent, "Data soal berhasil diperbarui!");
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            try {
                // Try to find if exception was rollback-only
            } catch (Exception ex) {
            }

            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Gagal memperbarui data soal: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
