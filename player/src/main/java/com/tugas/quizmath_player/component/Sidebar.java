package com.tugas.quizmath_player.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Sidebar extends JPanel {
    private JButton btnSoal;
    private JButton btnLeaderboard;
    private JButton btnNilai;
    private JButton btnDaftarSiswa;
    private JButton btnManageAdmin;
    private JButton btnSettings;
    private JButton btnLogout;

    public Sidebar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(44, 62, 80));
        setPreferredSize(new Dimension(200, 600));

        // Header
        JLabel header = new JLabel("ADMIN");
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setForeground(Color.WHITE);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));
        add(header);

        // Menu Buttons
        btnSoal = createMenuButton("Soal");
        btnLeaderboard = createMenuButton("Leaderboard");
        btnNilai = createMenuButton("Nilai");
        btnDaftarSiswa = createMenuButton("Daftar Siswa");
        btnManageAdmin = createMenuButton("Kelola Admin");
        btnSettings = createMenuButton("Pengaturan");
        btnLogout = createMenuButton("Logout");

        // Add Buttons to Panel
        add(btnSoal);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnLeaderboard);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnNilai);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnDaftarSiswa);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnManageAdmin);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnSettings);
        
        // Spacer to push Logout to bottom
        add(Box.createVerticalGlue());
        
        add(btnLogout);
        add(Box.createRigidArea(new Dimension(0, 20))); // Padding bottom
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 94));
            }
        });

        return btn;
    }

    public void addSoalListener(ActionListener listener) {
        btnSoal.addActionListener(listener);
    }

    public void addLeaderboardListener(ActionListener listener) {
        btnLeaderboard.addActionListener(listener);
    }

    public void addNilaiListener(ActionListener listener) {
        btnNilai.addActionListener(listener);
    }

    public void addDaftarSiswaListener(ActionListener listener) {
        btnDaftarSiswa.addActionListener(listener);
    }
    
    public void addManageAdminListener(ActionListener listener) {
        btnManageAdmin.addActionListener(listener);
    }
    
    public void addSettingsListener(ActionListener listener) {
        btnSettings.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) {
        btnLogout.addActionListener(listener);
    }
}