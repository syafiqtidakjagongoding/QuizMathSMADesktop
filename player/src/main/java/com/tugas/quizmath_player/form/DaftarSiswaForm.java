package com.tugas.quizmath_player.form;

import com.tugas.quizmath_player.entity.Kelas;
import com.tugas.quizmath_player.entity.Siswa;
import com.tugas.quizmath_player.repository.DataSiswaRepository;
import com.tugas.quizmath_player.repository.KelasRepository;
import com.tugas.quizmath_player.utils.MultiLineCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class DaftarSiswaForm extends JPanel {

    private final DataSiswaRepository siswa_repo;
    private final KelasRepository kelas_repo;
    
    private JTextField txtNama, txtNis, txtUsername, txtPassword, txtNoAbsen;
    private JComboBox<Kelas> cmbKelas;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnEdit, btnDelete, btnClear;

    private int selectedRow = -1;
    private int selectedIdSiswa = -1;

    public DaftarSiswaForm() {
        this.siswa_repo = new DataSiswaRepository();
        this.kelas_repo = new KelasRepository();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel title = new JLabel("Manajemen Data Siswa");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = createFormPanel();
        
        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tablePanel);
        splitPane.setDividerLocation(350); // Set initial width for form
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.0); // Keep form fixed size roughly
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Input Siswa"));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Nama
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nama:"), gbc);
        gbc.gridy = 1;
        txtNama = new JTextField();
        panel.add(txtNama, gbc);

        // NIS
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("NIS:"), gbc);
        gbc.gridy = 3;
        txtNis = new JTextField();
        panel.add(txtNis, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridy = 5;
        txtUsername = new JTextField();
        panel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridy = 7;
        txtPassword = new JTextField(); // Simple text for simplicity as per request
        panel.add(txtPassword, gbc);
        
        // No Absen
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("No Absen:"), gbc);
        gbc.gridy = 9;
        txtNoAbsen = new JTextField();
        panel.add(txtNoAbsen, gbc);

        // Kelas
        gbc.gridx = 0; gbc.gridy = 10;
        panel.add(new JLabel("Kelas:"), gbc);
        gbc.gridy = 11;
        cmbKelas = new JComboBox<>();
        loadKelasData();
        panel.add(cmbKelas, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        
        btnAdd = new JButton("Tambah");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> addSiswa());

        btnEdit = new JButton("Edit");
        btnEdit.setBackground(new Color(241, 196, 15));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.addActionListener(e -> editSiswa());

        btnDelete = new JButton("Hapus");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> deleteSiswa());
        
        btnClear = new JButton("Reset");
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 12;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(btnPanel, gbc);

        // Spacer
        gbc.gridy = 13;
        gbc.weighty = 1.0;
        panel.add(new JLabel(), gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Siswa"));
        
         String[] columnNames = {
            "ID", "Nama", "Username", "Password", "No Absen", "NIS", "Kelas", "Jurusan"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
             public boolean isCellEditable(int row, int column) {
                return false;
             }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        table.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new MultiLineCellRenderer());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectRow();
            }
        });
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadKelasData() {
        cmbKelas.removeAllItems();
        List<Kelas> kelass = kelas_repo.getAllKelas(this);
        for (Kelas k : kelass) {
            cmbKelas.addItem(k);
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Siswa> siswas = siswa_repo.getAllSiswa(this);
        for (Siswa s : siswas) {
            Object[] row = {
                s.getId(),
                s.getNama(),
                s.getUsername(),
                s.getPassword(),
                s.getNoAbsen(),
                s.getNis(),
                s.getNamaKelas(),
                s.getJurusan()
            };
            tableModel.addRow(row);
        }
    }
    
    private void selectRow() {
        selectedRow = table.getSelectedRow();
        if (selectedRow > -1) {
            selectedIdSiswa = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            txtNama.setText(table.getValueAt(selectedRow, 1).toString());
            txtUsername.setText(table.getValueAt(selectedRow, 2).toString());
            txtPassword.setText(table.getValueAt(selectedRow, 3).toString());
            txtNoAbsen.setText(table.getValueAt(selectedRow, 4).toString());
            txtNis.setText(table.getValueAt(selectedRow, 5).toString());
            
            String namaKelas = table.getValueAt(selectedRow, 6).toString();
            for (int i=0; i<cmbKelas.getItemCount(); i++) {
                Kelas k = cmbKelas.getItemAt(i);
                if (k.getKelas().equals(namaKelas)) {
                    cmbKelas.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private boolean validateForm() {
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong."); return false;
        }
        if (txtNis.getText().trim().isEmpty() || !txtNis.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "NIS harus angka."); return false;
        }
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username tidak boleh kosong."); return false;
        }
        if (txtPassword.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password tidak boleh kosong."); return false;
        }
        if (txtNoAbsen.getText().trim().isEmpty() || !txtNoAbsen.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "No Absen harus angka."); return false;
        }
        if (cmbKelas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih Kelas."); return false;
        }
        return true;
    }

    private void addSiswa() {
        if (!validateForm()) return;
        
        Kelas selectedKelas = (Kelas) cmbKelas.getSelectedItem();
        Siswa siswa = new Siswa(
                0,
                txtNama.getText().trim(),
                txtUsername.getText().trim(),
                txtPassword.getText().trim(),
                txtNis.getText().trim(),
                Integer.parseInt(txtNoAbsen.getText().trim()),
                selectedKelas
        );
        
        siswa_repo.buatSiswa(siswa, selectedKelas.getId(), this);
        loadData();
        clearForm();
    }

    private void editSiswa() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih siswa yg akan diedit."); return;
        }
        if (!validateForm()) return;
        
         Kelas selectedKelas = (Kelas) cmbKelas.getSelectedItem();
         Siswa siswa = new Siswa(
                selectedIdSiswa,
                txtNama.getText().trim(),
                txtUsername.getText().trim(),
                txtPassword.getText().trim(),
                txtNis.getText().trim(),
                Integer.parseInt(txtNoAbsen.getText().trim()),
                selectedKelas
        );
         
        siswa_repo.updateSiswa(siswa, selectedKelas.getId(), this);
        loadData();
        clearForm();
    }

    private void deleteSiswa() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih siswa yg akan dihapus."); return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus siswa?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
             siswa_repo.deleteSiswa(selectedIdSiswa, this);
             loadData();
             clearForm();
        }
    }

    private void clearForm() {
        txtNama.setText("");
        txtNis.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtNoAbsen.setText("");
        cmbKelas.setSelectedIndex(-1);
        selectedRow = -1;
        selectedIdSiswa = -1;
        table.clearSelection();
    }
}
