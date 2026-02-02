package com.tugas.quizmath_player.repository;

import com.tugas.quizmath_player.database.Database;
import com.tugas.quizmath_player.entity.Admin;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.util.List;

/**
 *
 * @author syafiq
 */
public class AdminRepository {
    public boolean checkLogin(Admin admin) {
        try (Session session = Database.getSessionFactory().openSession()) {
            String hql = "FROM Admin WHERE username = :username AND password = :password";
            Query<Admin> query = session.createQuery(hql, Admin.class);
            query.setParameter("username", admin.getUsername());
            query.setParameter("password", admin.getPassword());
            return query.uniqueResult() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Admin getAdminByUsername(String username) {
        try (Session session = Database.getSessionFactory().openSession()) {
            String hql = "FROM Admin WHERE username = :username";
            Query<Admin> query = session.createQuery(hql, Admin.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Admin> getAllAdmins(Component parent) {
        try (Session session = Database.getSessionFactory().openSession()) {
            return session.createQuery("FROM Admin", Admin.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error loading admins: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }
    
    public void createAdmin(Admin admin, Component parent) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(admin);
            tx.commit();
            JOptionPane.showMessageDialog(parent, "Admin berhasil ditambahkan!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error creating admin: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void updatePassword(String username, String newPassword, Component parent) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Admin admin = getAdminByUsername(username);
            if (admin != null) {
                admin.setPassword(newPassword);
                session.merge(admin);
                tx.commit();
                JOptionPane.showMessageDialog(parent, "Password berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(parent, "Admin tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error updating password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteAdmin(int adminId, Component parent) {
        Transaction tx = null;
        try (Session session = Database.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Admin admin = session.get(Admin.class, adminId);
            if (admin != null) {
                session.remove(admin);
                tx.commit();
                JOptionPane.showMessageDialog(parent, "Admin berhasil dihapus!");
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error deleting admin: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
