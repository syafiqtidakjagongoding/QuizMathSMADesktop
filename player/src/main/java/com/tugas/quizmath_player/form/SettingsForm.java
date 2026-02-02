package com.tugas.quizmath_player.form;

import com.tugas.quizmath_player.helper.Session;
import com.tugas.quizmath_player.repository.AdminRepository;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class SettingsForm extends JPanel {

    private AdminRepository adminRepo;
    private JPasswordField txtOldPassword, txtNewPassword, txtConfirmPassword;
    private JButton btnSave, btnCancel;

    public SettingsForm() {
        this.adminRepo = new AdminRepository();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Pengaturan Akun");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Ubah Password"));
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new java.awt.Dimension(500, 300));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Current Username (read-only display)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        JLabel lblUsername = new JLabel(Session.getCurrentAdminUsername());
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblUsername, gbc);

        // Old Password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password Lama:"), gbc);
        
        gbc.gridx = 1;
        txtOldPassword = new JPasswordField(20);
        formPanel.add(txtOldPassword, gbc);

        // New Password
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password Baru:"), gbc);
        
        gbc.gridx = 1;
        txtNewPassword = new JPasswordField(20);
        formPanel.add(txtNewPassword, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Konfirmasi Password:"), gbc);
        
        gbc.gridx = 1;
        txtConfirmPassword = new JPasswordField(20);
        formPanel.add(txtConfirmPassword, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        
        btnSave = new JButton("Simpan");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new java.awt.Dimension(100, 35));
        btnSave.addActionListener(e -> changePassword());

        btnCancel = new JButton("Batal");
        btnCancel.setPreferredSize(new java.awt.Dimension(100, 35));
        btnCancel.addActionListener(e -> clearForm());
        
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        formPanel.add(btnPanel, gbc);

        // Center the form panel
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        GridBagConstraints wrapperGbc = new GridBagConstraints();
        centerWrapper.add(formPanel, wrapperGbc);
        
        add(centerWrapper, BorderLayout.CENTER);
    }

    private boolean validateForm() {
        String oldPassword = new String(txtOldPassword.getPassword());
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        if (oldPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password lama harus diisi.");
            return false;
        }
        
        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password baru harus diisi.");
            return false;
        }
        
        if (newPassword.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password baru minimal 4 karakter.");
            return false;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Password baru dan konfirmasi tidak cocok.");
            return false;
        }
        
        // Verify old password
        com.tugas.quizmath_player.entity.Admin admin = new com.tugas.quizmath_player.entity.Admin(
            Session.getCurrentAdminUsername(),
            oldPassword
        );
        
        if (!adminRepo.checkLogin(admin)) {
            JOptionPane.showMessageDialog(this, "Password lama salah.");
            return false;
        }
        
        return true;
    }

    private void changePassword() {
        if (!validateForm()) return;
        
        String newPassword = new String(txtNewPassword.getPassword());
        adminRepo.updatePassword(Session.getCurrentAdminUsername(), newPassword, this);
        clearForm();
    }

    private void clearForm() {
        txtOldPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
    }
}
