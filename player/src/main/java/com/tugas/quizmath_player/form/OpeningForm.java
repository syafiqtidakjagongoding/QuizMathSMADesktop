package com.tugas.quizmath_player.form;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class OpeningForm extends JFrame {
  

        // Data dummy untuk testing - nanti ganti dengan data dari Session
        
        // this("John Doe", "2023001", "5", "XII", "RPL");

    public OpeningForm(String nama, String nis, String absen, String kelas, String jurusan) {
        // this.nama = nama;
        // this.nis = nis;
        // this.absen = absen;
        // this.kelas = kelas;
        // this.jurusan = jurusan;

        setTitle("Quiz Game - Verifikasi Data");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Panel utama dengan gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(52, 152, 219);
                Color color2 = new Color(41, 128, 185);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Panel konten dengan background putih
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 73, 94), 4),
                new EmptyBorder(50, 80, 50, 80)));

        // Icon user (optional - bisa diganti dengan foto siswa)
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Judul
        JLabel titleLabel = new JLabel("VERIFIKASI DATA SISWA");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subjudul
        JLabel subtitleLabel = new JLabel("Pastikan data Anda sudah benar sebelum memulai quiz");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(40));

        // Panel untuk data siswa
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(5, 2, 20, 20));
        dataPanel.setOpaque(false);
        dataPanel.setMaximumSize(new Dimension(700, 300));

        // Styling untuk label
        Font labelFont = new Font("Arial", Font.BOLD, 22);
        Font valueFont = new Font("Arial", Font.PLAIN, 22);
        Color labelColor = new Color(52, 73, 94);
        Color valueColor = new Color(41, 128, 185);

        // Nama
        JLabel lblNama = new JLabel("Nama:");
        lblNama.setFont(labelFont);
        lblNama.setForeground(labelColor);
        JLabel valNama = new JLabel(nama);
        valNama.setFont(valueFont);
        valNama.setForeground(valueColor);

        // NIS
        JLabel lblNis = new JLabel("NIS:");
        lblNis.setFont(labelFont);
        lblNis.setForeground(labelColor);
        JLabel valNis = new JLabel(nis);
        valNis.setFont(valueFont);
        valNis.setForeground(valueColor);

        // Absen
        JLabel lblAbsen = new JLabel("Absen:");
        lblAbsen.setFont(labelFont);
        lblAbsen.setForeground(labelColor);
        JLabel valAbsen = new JLabel(absen);
        valAbsen.setFont(valueFont);
        valAbsen.setForeground(valueColor);

        // Kelas
        JLabel lblKelas = new JLabel("Kelas:");
        lblKelas.setFont(labelFont);
        lblKelas.setForeground(labelColor);
        JLabel valKelas = new JLabel(kelas);
        valKelas.setFont(valueFont);
        valKelas.setForeground(valueColor);

        // Jurusan
        JLabel lblJurusan = new JLabel("Jurusan:");
        lblJurusan.setFont(labelFont);
        lblJurusan.setForeground(labelColor);
        JLabel valJurusan = new JLabel(jurusan);
        valJurusan.setFont(valueFont);
        valJurusan.setForeground(valueColor);

        dataPanel.add(lblNama);
        dataPanel.add(valNama);
        dataPanel.add(lblNis);
        dataPanel.add(valNis);
        dataPanel.add(lblAbsen);
        dataPanel.add(valAbsen);
        dataPanel.add(lblKelas);
        dataPanel.add(valKelas);
        dataPanel.add(lblJurusan);
        dataPanel.add(valJurusan);

        contentPanel.add(dataPanel);
        contentPanel.add(Box.createVerticalStrut(50));

        // Panel untuk tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tombol Keluar
        JButton btnKeluar = createButton("KELUAR", new Color(231, 76, 60), new Color(192, 57, 43));
        btnKeluar.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah Anda yakin ingin keluar?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Kembali ke login atau tutup aplikasi
                new LoginForm().setVisible(true); // Uncomment jika ada LoginForm
                this.dispose();
            }
        });

        // Tombol Lanjut
        JButton btnLanjut = createButton("LANJUT", new Color(46, 204, 113), new Color(39, 174, 96));
        btnLanjut.addActionListener(e -> {
            // Pindah ke QuizLevelSelection
            new QuizLevelSelection().setVisible(true);
            this.dispose();
        });

        buttonPanel.add(btnKeluar);
        buttonPanel.add(btnLanjut);

        contentPanel.add(buttonPanel);

        // Info tambahan di bawah
        contentPanel.add(Box.createVerticalStrut(30));
        JLabel infoLabel = new JLabel("Jika data tidak sesuai, silakan hubungi administrator");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        infoLabel.setForeground(new Color(149, 165, 166));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(infoLabel);

        // Tambahkan content panel ke main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(contentPanel, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                if (getModel().isPressed()) {
                    g2d.setColor(color2);
                } else if (getModel().isRollover()) {
                    GradientPaint gp = new GradientPaint(0, 0, color1.brighter(), 0, getHeight(), color2.brighter());
                    g2d.setPaint(gp);
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                    g2d.setPaint(gp);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(180, 55));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // Method untuk set data siswa dari luar
    // public void setStudentData(String nama, String nis, String absen, String kelas, String jurusan) {
    //     this.nama = nama;
    //     this.nis = nis;
    //     this.absen = absen;
    //     this.kelas = kelas;
    //     this.jurusan = jurusan;
    // }

    // public static void main(String[] args) {
    //         // Contoh penggunaan dengan data dummy
    //         new OpeningForm("Budi Santoso", "2023001", "12", "XII", "RPL");

    //         // Atau bisa dari Session
    //         // String nama = Session.getCurrentSiswaName();
    //         // String nis = Session.getCurrentSiswaNis();
    //         // String absen = Session.getCurrentSiswaAbsen();
    //         // String kelas = Session.getCurrentSiswaKelas();
    //         // String jurusan = Session.getCurrentSiswaJurusan();
    //         // new OpeningForm(nama, nis, absen, kelas, jurusan);
    // }
}