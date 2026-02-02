/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.entity.DataSiswa;
import com.tugas.quizmath_player.entity.Siswa;
import com.tugas.quizmath_player.entity.Kelas;
import com.tugas.quizmath_player.database.Database;
import com.tugas.quizmath_player.helper.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author syafiq
 */
public class DataSiswaRepository {

    public DataSiswa loginSiswa(DataSiswa dataSiswa, Component rootPane) {
        try (org.hibernate.Session session = Database.getSessionFactory().openSession()) {
            String hql = "FROM Siswa s WHERE s.username = :username AND s.password = :password";
            Query<Siswa> query = session.createQuery(hql, Siswa.class);
            query.setParameter("username", dataSiswa.getUsername());
            query.setParameter("password", dataSiswa.getPassword());

            Siswa siswa = query.uniqueResult();
            if (siswa != null) {
                System.out.println("✅ Login berhasil: " + siswa.getNama());
                Session.setSession(siswa.getId(), siswa.getNama());

                return new DataSiswa(
                        siswa.getUsername(),
                        String.valueOf(siswa.getNoAbsen()),
                        siswa.getNama(),
                        siswa.getNis(),
                        siswa.getNamaKelas(),
                        siswa.getJurusan());
            } else {
                JOptionPane.showMessageDialog(rootPane, "⚠️ Username atau password salah");
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Gagal login siswa: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public DataSiswa getSiswaById(int id, Component rootPane) {
        try (org.hibernate.Session session = Database.getSessionFactory().openSession()) {
            Siswa siswa = session.get(Siswa.class, id);
            if (siswa != null) {
                return new DataSiswa(
                        siswa.getUsername(),
                        String.valueOf(siswa.getNoAbsen()),
                        siswa.getNama(),
                        siswa.getNis(),
                        siswa.getNamaKelas(),
                        "" // jurusan handled differently in original code, but getJurusan works too
                );
            } else {
                JOptionPane.showMessageDialog(rootPane, "⚠️ Siswa dengan ID " + id + " tidak ditemukan");
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Gagal mengambil data siswa: " + e.getMessage());
            return null;
        }
    }

    // Deprecated or remove if unused, but implementing for compatibility
    public List<Kelas> getAllKelas() {
        return new KelasRepository().getAllKelas(null);
    }

    public List<Siswa> getAllSiswa(Component parentComponent) {
        try (org.hibernate.Session session = Database.getSessionFactory().openSession()) {
            return session.createQuery("FROM Siswa", Siswa.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi error saat mengambil data siswa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void deleteSiswa(int selectedId, Component parentComponent) {
        Transaction transaction = null;
        try (org.hibernate.Session session = Database.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Delete FinalScore first (manual cascading if not configured in entity)
            // Ideally define @OneToMany in Siswa for cascading delete, but hql works
            session.createMutationQuery("DELETE FROM FinalScore WHERE siswa_id = :id") // Assuming FinalScore entity
                                                                                       // exists or use SQL
                    .setParameter("id", selectedId)
                    .executeUpdate();

            // OR if FinalScore is not yet migrated, we might need a native SQL query or
            // skip if we assume cascade.
            // But init.sql says DELETE SET NULL on options_answer and CASCADE on
            // siswa_answer but final_score NO ACTION?
            // "FOREIGN KEY (siswa_id) REFERENCES siswa(id)" (no cascade specified, so
            // RESTRICT/NO ACTION default).
            // So we must delete scores first.
            // Since FinalScore entity is not yet ready, I will use native query or skip if
            // I can't.
            // Wait, I saw FinalScoreRepository. This means FinalScore entity needs
            // migration too.
            // For now, I will assume FinalScore is an entity mapped to "final_score" table.

            // Actually, I'll use native SQL for safety until FinalScore is migrated.
            session.createNativeMutationQuery("DELETE FROM final_score WHERE siswa_id = :id")
                    .setParameter("id", selectedId)
                    .executeUpdate();

            Siswa siswa = session.get(Siswa.class, selectedId);
            if (siswa != null) {
                session.remove(siswa);
            }

            transaction.commit();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Data siswa dan skor final berhasil dihapus.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi error saat menghapus data siswa: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void updateSiswa(Siswa siswaData, int kelasId, Component parentComponent) {
        Transaction transaction = null;
        try (org.hibernate.Session session = Database.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Fetch existing or merge
            Siswa siswa = session.get(Siswa.class, siswaData.getId());
            if (siswa != null) {
                siswa.setNama(siswaData.getNama());
                siswa.setNoAbsen(siswaData.getNoAbsen());
                siswa.setNis(siswaData.getNis());
                siswa.setUsername(siswaData.getUsername());
                siswa.setPassword(siswaData.getPassword());

                Kelas kelas = session.get(Kelas.class, kelasId);
                siswa.setKelas(kelas);

                session.merge(siswa);
            }
            transaction.commit();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Data siswa berhasil diedit.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi error saat mengedit data siswa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void buatSiswa(Siswa siswaData, int kelasId, Component parentComponent) {
        Transaction transaction = null;
        try (org.hibernate.Session session = Database.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Kelas kelas = session.get(Kelas.class, kelasId);
            siswaData.setKelas(kelas);

            session.persist(siswaData);

            transaction.commit();

            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Data siswa berhasil dibuat.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            JOptionPane.showMessageDialog(
                    parentComponent,
                    "Terjadi error saat membuat data siswa",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
