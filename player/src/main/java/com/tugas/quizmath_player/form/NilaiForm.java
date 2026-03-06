package com.tugas.quizmath_player.form;

import com.tugas.quizmath_player.entity.DetailedAnswer;
import com.tugas.quizmath_player.entity.Leaderboard;
import com.tugas.quizmath_player.repository.FinalScoreRepository;
import com.tugas.quizmath_player.repository.SiswaAnswerRepository;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class NilaiForm extends JPanel {

    private FinalScoreRepository fscore_repo;
    private SiswaAnswerRepository siswaAnswer_repo;
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JTable detailTable;
    private DefaultTableModel detailTableModel;
    private JComboBox<String> cmbFilter;
    private DatePicker datePicker;
    private JButton btnRefresh;
    private JLabel lblDetailTitle;
    
    private List<Leaderboard> currentData;
    private int selectedFinalScoreId = -1;

    public NilaiForm() {
        this.fscore_repo = new FinalScoreRepository();
        this.siswaAnswer_repo = new SiswaAnswerRepository();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Data Nilai Siswa");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Left Panel: Student List
        JPanel leftPanel = createStudentListPanel();
        
        // Right Panel: Answer Details
        JPanel rightPanel = createDetailPanel();

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.4);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createStudentListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Siswa"));
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        
        filterPanel.add(new JLabel("Urutkan:"));
        
        cmbFilter = new JComboBox<>(new String[]{"Terbaru", "Nilai Tertinggi", "Nilai Terendah"});
        cmbFilter.addActionListener(e -> sortData());
        filterPanel.add(cmbFilter);

        filterPanel.add(new JLabel("Tanggal:"));
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dateSettings.setFormatForDatesBeforeCommonEra("yyyy-MM-dd");
        datePicker = new DatePicker(dateSettings);
        datePicker.addDateChangeListener(e -> sortData());
        filterPanel.add(datePicker);

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadData());
        filterPanel.add(btnRefresh);

        // Table
        String[] columnNames = {
                "ID", "Nama", "NIS", "Kelas", "Benar", "Salah", "Total", "Nilai", "Dibuat"
        };
        studentTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 4 || columnIndex == 5 || columnIndex == 6 || columnIndex == 7)
                    return Integer.class;
                return String.class;
            }
        };

        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(30);
        studentTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        
        // Hide ID
        studentTable.getColumnModel().getColumn(0).setMinWidth(0);
        studentTable.getColumnModel().getColumn(0).setMaxWidth(0);
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        // Add selection listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadStudentDetails();
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Detail Jawaban"));
        
        // Title label for selected student
        lblDetailTitle = new JLabel("Pilih siswa untuk melihat detail jawaban");
        lblDetailTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblDetailTitle.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblDetailTitle, BorderLayout.NORTH);
        
        // Detail table
        String[] columnNames = {
            "No", "Pertanyaan", "Jawaban Siswa", "Status", "Jawaban Benar"
        };
        detailTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        detailTable = new JTable(detailTableModel);
        detailTable.setRowHeight(40);
        detailTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        detailTable.getColumnModel().getColumn(0).setMaxWidth(60);
        detailTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        detailTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        detailTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        detailTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        // Custom renderer for Status column with colors
        detailTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String status = value.toString();
                    if (status.contains("✓")) {
                        c.setForeground(new Color(46, 204, 113)); // Green
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else if (status.contains("✗")) {
                        c.setForeground(new Color(231, 76, 60)); // Red
                        setFont(getFont().deriveFont(Font.BOLD));
                    }
                }
                
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                } else {
                    c.setBackground(Color.WHITE);
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(detailTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadData() {
        new SwingWorker<List<Leaderboard>, Void>() {
            @Override
            protected List<Leaderboard> doInBackground() {
                return fscore_repo.getAllScore(NilaiForm.this);
            }

            @Override
            protected void done() {
                try {
                    studentTableModel.setRowCount(0);
                    List<Leaderboard> list = get();
                    populateTable(list);
                    sortData();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(NilaiForm.this, "Error loading data: " + e.getMessage());
                }
            }
        }.execute();
    }
    
    private void populateTable(List<Leaderboard> list) {
        this.currentData = list;
        studentTableModel.setRowCount(0);
        if (list == null) return;
        
        for (Leaderboard l : list) {
            Object[] row = {
                l.id,
                l.siswa,
                l.nis,
                l.kelas,
                l.correct_anwer,
                l.wrong_anwer,
                l.total_question,
                    l.final_score,
                    l.createdAt != null
                            ? new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", new java.util.Locale("id", "ID"))
                                    .format(l.createdAt)
                            : ""
            };
            System.out.println(row);
            studentTableModel.addRow(row);
        }
    }

    private void sortData() {
        if (currentData == null) return;
        String filter = (String) cmbFilter.getSelectedItem();
        
        if ("Nilai Tertinggi".equals(filter)) {
            currentData.sort(Comparator.comparingDouble((Leaderboard l) -> l.final_score).reversed());
        } else if ("Nilai Terendah".equals(filter)) {
            currentData.sort(Comparator.comparingDouble((Leaderboard l) -> l.final_score));
        } else if ("Terbaru".equals(filter)) {
            currentData.sort(Comparator.comparingInt((Leaderboard l) -> l.id).reversed());
        }

        // // Apply Date filter if selected
        java.time.LocalDate selectedDate = datePicker.getDate();
        studentTableModel.setRowCount(0);
        for (Leaderboard l : currentData) {
            if (selectedDate == null
                    || (l.createdAt != null && l.createdAt.toLocalDateTime().toLocalDate().equals(selectedDate))) {
                Object[] row = {
                        l.id, l.siswa, l.nis, l.kelas, l.correct_anwer, l.wrong_anwer, l.total_question, l.final_score,
                        l.createdAt != null
                                ? new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", new java.util.Locale("id", "ID"))
                                        .format(l.createdAt)
                                : ""
                };
                studentTableModel.addRow(row);
            }
        }
    }
    
    private void loadStudentDetails() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            detailTableModel.setRowCount(0);
            lblDetailTitle.setText("Pilih siswa untuk melihat detail jawaban");
            return;
        }
        
        selectedFinalScoreId = (int) studentTable.getValueAt(selectedRow, 0);
        String studentName = (String) studentTable.getValueAt(selectedRow, 1);
        int score = (int) studentTable.getValueAt(selectedRow, 7);
        
        lblDetailTitle.setText(String.format("Detail Jawaban: %s (Nilai: %d)", studentName, score));
        
        // Load details in background
        new SwingWorker<List<DetailedAnswer>, Void>() {
            @Override
            protected List<DetailedAnswer> doInBackground() {
                return siswaAnswer_repo.getStudentAnswers(selectedFinalScoreId, NilaiForm.this);
            }

            @Override
            protected void done() {
                try {
                    detailTableModel.setRowCount(0);
                    List<DetailedAnswer> details = get();
                    
                    for (DetailedAnswer detail : details) {
                        String status = detail.isCorrect() ? "✓ Benar" : "✗ Salah";
                        String correctAnswer = detail.isCorrect() ? "-" : detail.getCorrectAnswer();
                        
                        // Truncate question text if too long
                        String questionText = detail.getQuestionText();
                        if (questionText.length() > 100) {
                            questionText = questionText.substring(0, 97) + "...";
                        }
                        
                        Object[] row = {
                            detail.getQuestionNumber(),
                            questionText,
                            detail.getStudentAnswer(),
                            status,
                            correctAnswer
                        };
                        detailTableModel.addRow(row);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(NilaiForm.this, 
                        "Error loading answer details: " + e.getMessage());
                }
            }
        }.execute();
    }
}
