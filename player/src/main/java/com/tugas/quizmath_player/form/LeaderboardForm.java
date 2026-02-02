package com.tugas.quizmath_player.form;

import com.tugas.quizmath_player.entity.Leaderboard;
import com.tugas.quizmath_player.repository.FinalScoreRepository;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

public class LeaderboardForm extends JPanel {

    private FinalScoreRepository fscore_repo;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;
    private JButton btnRefresh;

    public LeaderboardForm() {
        this.fscore_repo = new FinalScoreRepository();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Leaderboard Siswa");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Top Panel (Search)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        
        topPanel.add(new JLabel("Cari (Nama/NIS):"));
        txtSearch = new JTextField(20);
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchTable();
            }
        });
        topPanel.add(txtSearch);

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh);

        // Table
        String[] columnNames = {
            "ID", "Rank", "Nama", "NIS", "Kelas", "Benar", "Salah", "Total Soal", "Nilai Akhir"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 1 || columnIndex == 5 || columnIndex == 6 || columnIndex == 7) return Integer.class;
                if (columnIndex == 8) return Double.class;
                return String.class;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        // Hide ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        // Rank column small width
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(80);

        JScrollPane scrollPane = new JScrollPane(table);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void loadData() {
         new SwingWorker<List<Leaderboard>, Void>() {
            @Override
            protected List<Leaderboard> doInBackground() {
                return fscore_repo.getAllScore(LeaderboardForm.this);
            }

            @Override
            protected void done() {
                try {
                    tableModel.setRowCount(0);
                    List<Leaderboard> list = get();
                    
                    // Sort by score desc initially to assign rank
                    list.sort((a, b) -> Double.compare(b.final_score, a.final_score));
                    
                    int rank = 1;
                    for (Leaderboard l : list) {
                        Object[] row = {
                            l.id,
                            rank++,
                            l.siswa,
                            l.nis,
                            l.kelas,
                            l.correct_anwer,
                            l.wrong_anwer,
                            l.total_question,
                            l.final_score
                        };
                        tableModel.addRow(row);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(LeaderboardForm.this, "Error loading data: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void searchTable() {
        String text = txtSearch.getText().trim();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            // Case insensitive search on Nama (2) and NIS (3)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 2, 3));
        }
    }
}
