/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.tugas.quizmath_player.form;

import com.tugas.quizmath_player.helper.BackgroundPanel;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author syafiq
 */
public class LoadingForm extends javax.swing.JFrame {

    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel titleLabel;

    public LoadingForm() {
        initComponents();

        setTitle("Loading...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        // 🖼 Ambil gambar background
        Image bg = new ImageIcon(getClass().getResource("/images/FuseMath.png")).getImage();

        // 🎨 Gunakan panel background
        BackgroundPanel background = new BackgroundPanel(bg, true, 0);
        background.setLayout(new BorderLayout());
        setContentPane(background);

        // 🎨 Ubah warna ProgressBar
        UIManager.put("ProgressBar.foreground", new Color(52, 152, 219)); // biru cerah
        UIManager.put("ProgressBar.background", new Color(255, 255, 255, 120));
        UIManager.put("ProgressBar.selectionBackground", Color.WHITE);
        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);

        // 🏷 Title "Fuse Quiz"
        titleLabel = new JLabel("Fuse Quiz", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));

        // panel transparan agar tulisan lebih terbaca
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.CENTER);

        background.add(topPanel, BorderLayout.NORTH);

        // 🔥 Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(600, 30));
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // panel transparan untuk progressbar
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));
        bottomPanel.add(progressBar, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        revalidate();
    }

    public void startLoading() {
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                final int value = i;
                SwingUtilities.invokeLater(() -> progressBar.setValue(value));

                try {
                    Thread.sleep(15); // sedikit lebih lambat biar smooth
                } catch (InterruptedException ignored) {
                }
            }

            SwingUtilities.invokeLater(() -> {
                dispose();
                new LoginForm().setVisible(true);
            });
        }).start();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        pack();
    }
}
