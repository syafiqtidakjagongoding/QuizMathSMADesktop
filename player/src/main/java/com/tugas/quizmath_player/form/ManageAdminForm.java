package com.tugas.quizmath_player.form;

import com.tugas.quizmath_player.entity.Admin;
import com.tugas.quizmath_player.repository.AdminRepository;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ManageAdminForm extends JPanel {

    private AdminRepository adminRepo;
    private JTextField txtUsername;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnDelete, btnClear, btnRefresh;
    
    private int selectedRow = -1;
    private int selectedAdminId = -1;

    public ManageAdminForm() {
        this.adminRepo = new AdminRepository();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel title = new JLabel("Manajemen Admin");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = createFormPanel();
        
        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tablePanel);
        splitPane.setDividerLocation(350);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.0);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Tambah Admin Baru"));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 1;
        txtUsername = new JTextField();
        panel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridy = 3;
        txtPassword = new JPasswordField();
        panel.add(txtPassword, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Konfirmasi Password:"), gbc);
        gbc.gridy = 5;
        txtConfirmPassword = new JPasswordField();
        panel.add(txtConfirmPassword, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        
        btnAdd = new JButton("Tambah Admin");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addAdmin());

        btnDelete = new JButton("Hapus Admin");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> deleteAdmin());
        
        btnClear = new JButton("Reset");
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(btnPanel, gbc);

        // Spacer
        gbc.gridy = 7;
        gbc.weighty = 1.0;
        panel.add(new JLabel(), gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Admin"));
        
        String[] columnNames = {"ID", "Username"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        
        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectRow();
            }
        });
        
        // Refresh button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Admin> admins = adminRepo.getAllAdmins(this);
        for (Admin admin : admins) {
            Object[] row = {admin.getId(), admin.getUsername()};
            tableModel.addRow(row);
        }
    }
    
    private void selectRow() {
        selectedRow = table.getSelectedRow();
        if (selectedRow > -1) {
            selectedAdminId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            txtUsername.setText(table.getValueAt(selectedRow, 1).toString());
        }
    }

    private boolean validateForm() {
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username tidak boleh kosong.");
            return false;
        }
        
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password tidak boleh kosong.");
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Password dan konfirmasi password tidak cocok.");
            return false;
        }
        
        return true;
    }

    private void addAdmin() {
        if (!validateForm()) return;
        
        Admin admin = new Admin(
            txtUsername.getText().trim(),
            new String(txtPassword.getPassword())
        );
        
        adminRepo.createAdmin(admin, this);
        loadData();
        clearForm();
    }

    private void deleteAdmin() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih admin yang akan dihapus.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Yakin hapus admin ini?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            adminRepo.deleteAdmin(selectedAdminId, this);
            loadData();
            clearForm();
        }
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        selectedRow = -1;
        selectedAdminId = -1;
        table.clearSelection();
    }
}
