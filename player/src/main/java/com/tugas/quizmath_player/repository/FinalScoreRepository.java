/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.database.Database;
import com.tugas.quizmath_player.entity.FinalScore;
import com.tugas.quizmath_player.entity.Leaderboard;
import com.tugas.quizmath_player.entity.Siswa;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author syafiq
 */
public class FinalScoreRepository {

    public void calculateScore(Component parentComponent, int siswaId) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // 1. Calculate total benar
            String hqlBenar = "SELECT COUNT(sa) FROM SiswaAnswer sa WHERE sa.siswa.id = :sid AND sa.optionAnswer.correct = true";
            Long totalBenarLong = session.createQuery(hqlBenar, Long.class)
                    .setParameter("sid", siswaId)
                    .uniqueResult();
            int totalBenar = totalBenarLong != null ? totalBenarLong.intValue() : 0;

            // 2. Calculate total soal
            String hqlTotal = "SELECT COUNT(q) FROM Question q";
            Long totalSoalLong = session.createQuery(hqlTotal, Long.class).uniqueResult();
            int totalSoal = totalSoalLong != null ? totalSoalLong.intValue() : 0;

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

            // 3. Insert or Update FinalScore
            String hqlFind = "FROM FinalScore fs WHERE fs.siswa.id = :sid";
            FinalScore finalScore = session.createQuery(hqlFind, FinalScore.class)
                    .setParameter("sid", siswaId)
                    .uniqueResult();

            if (finalScore == null) {
                Siswa siswa = session.get(Siswa.class, siswaId);
                finalScore = new FinalScore(siswa, totalBenar, totalSalah, totalSoal, nilaiAkhir);
                session.persist(finalScore);
            } else {
                finalScore.setCorrectAnswer(totalBenar);
                finalScore.setWrongAnswer(totalSalah);
                finalScore.setTotalQuestion(totalSoal);
                finalScore.setFinalScore(nilaiAkhir);
                session.merge(finalScore);
            }

            tx.commit();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Soal sudah terkirim",
                    "Soal terkirim",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi kesalahan saat menghitung skor:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public FinalScore insertScore(Component parentComponent, int siswaId, int correctAnswer, int totalQuestion) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            int wrongAnswer = totalQuestion - correctAnswer;
            double finalScoreValue = ((double) correctAnswer / totalQuestion) * 100.0;

            Siswa siswa = session.get(Siswa.class, siswaId);
            FinalScore finalScore = new FinalScore(siswa, correctAnswer, wrongAnswer, totalQuestion, finalScoreValue);
            session.persist(finalScore);

            tx.commit();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Soal sudah terkirim!",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);

            return finalScore;

        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi kesalahan saat menyimpan skor:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    public List<Leaderboard> getAllScore(Component parentComponent) {
        List<Leaderboard> leaderboard = new ArrayList<>();
        try (Session session = Database.getSessionFactory().openSession()) {
            // Join FinalScore -> Siswa -> Kelas
            // Sort by finalScore DESC
            String hql = "FROM FinalScore fs JOIN FETCH fs.siswa s JOIN FETCH s.kelas k ORDER BY fs.finalScore DESC";
            List<FinalScore> scores = session.createQuery(hql, FinalScore.class).list();

            for (FinalScore fs : scores) {
                Leaderboard lboard = new Leaderboard(
                        fs.getId(), // or fs.getSiswa().getId()? Original used fs.id (rs.getInt("id"))
                        fs.getSiswa().getNama(),
                        fs.getSiswa().getNis(),
                        fs.getSiswa().getNamaKelas(), // using helper in Siswa
                        fs.getCorrectAnswer(),
                        fs.getWrongAnswer(),
                        fs.getTotalQuestion(),
                        (int) fs.getFinalScore(), // Cast to int as Leaderboard expects int
                        fs.getCreatedAt()
                );
                leaderboard.add(lboard);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent, "Gagal mengambil data final score: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return leaderboard;
    }
}
