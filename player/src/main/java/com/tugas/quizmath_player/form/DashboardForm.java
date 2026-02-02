package com.tugas.quizmath_player.form;

import com.tugas.quizmath_player.component.Sidebar;
import com.tugas.quizmath_player.helper.Session;

import javax.swing.*;
import java.awt.*;

public class DashboardForm extends JFrame {

    private Sidebar sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Panels
    private SoalForm soalPanel;
    private LeaderboardForm leaderboardPanel;
    private NilaiForm nilaiPanel;
    private DaftarSiswaForm daftarSiswaPanel;
    private ManageAdminForm manageAdminPanel;
    private SettingsForm settingsPanel;

    public DashboardForm() {
        setTitle("Dashboard Admin");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar
        sidebar = new Sidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Content panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Initialize Panels (Lazy loading or upfront)
        initPanels();
        
        // Add to main panel
        add(mainPanel);

        // Listeners
        initListeners();

        // Show default
        cardLayout.show(contentPanel, "Soal");
    }

    private void initPanels() {
        soalPanel = new SoalForm();
        leaderboardPanel = new LeaderboardForm();
        nilaiPanel = new NilaiForm();
        daftarSiswaPanel = new DaftarSiswaForm();
        manageAdminPanel = new ManageAdminForm();
        settingsPanel = new SettingsForm();

        contentPanel.add(soalPanel, "Soal");
        contentPanel.add(leaderboardPanel, "Leaderboard");
        contentPanel.add(nilaiPanel, "Nilai");
        contentPanel.add(daftarSiswaPanel, "DaftarSiswa");
        contentPanel.add(manageAdminPanel, "ManageAdmin");
        contentPanel.add(settingsPanel, "Settings");
    }

    private void initListeners() {
        sidebar.addSoalListener(e -> cardLayout.show(contentPanel, "Soal"));
        sidebar.addLeaderboardListener(e -> cardLayout.show(contentPanel, "Leaderboard"));
        sidebar.addNilaiListener(e -> cardLayout.show(contentPanel, "Nilai"));
        sidebar.addDaftarSiswaListener(e -> cardLayout.show(contentPanel, "DaftarSiswa"));
        sidebar.addManageAdminListener(e -> cardLayout.show(contentPanel, "ManageAdmin"));
        sidebar.addSettingsListener(e -> cardLayout.show(contentPanel, "Settings"));
        sidebar.addLogoutListener(e -> logout());
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            Session.clear();
            new LoginForm().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DashboardForm().setVisible(true);
        });
    }
}
