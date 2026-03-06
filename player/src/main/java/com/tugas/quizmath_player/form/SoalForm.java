package com.tugas.quizmath_player.form;

import com.tugas.quizmath_player.entity.QuestionManipulation;
import com.tugas.quizmath_player.repository.QuestionRepository;
import com.tugas.quizmath_player.utils.MultiLineCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class SoalForm extends JPanel {
    private QuestionRepository question_repo;
    private JTable tabelSoal;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;
    private JButton btnAdd, btnRefresh;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SoalForm.class.getName());

    public SoalForm() {
        this.question_repo = new QuestionRepository();
        initComponents();
        getSoal();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Data Soal");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Top Panel (Search & Buttons)
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Cari:"));
        txtSearch = new JTextField(25);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTable();
            }
        });
        searchPanel.add(txtSearch);

        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolPanel.setOpaque(false);
        
        btnAdd = new JButton("+ Tambah Soal");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> openTambahSoal(0)); // 0 means new

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> getSoal());

        toolPanel.add(btnAdd);
        toolPanel.add(btnRefresh);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(toolPanel, BorderLayout.EAST);

        // Table Setup
        String[] columnNames = {
            "ID", "Question", "Image", "Answer Type", "Level", "Topic", 
            "Correct Answer", "Answer A", "Answer B", "Answer C", "Answer D"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
             public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return ImageIcon.class;
                }
                return Object.class;
            }
        };

        tabelSoal = new JTable(tableModel);
        tabelSoal.setRowHeight(80);
        sorter = new TableRowSorter<>(tableModel);
        tabelSoal.setRowSorter(sorter);
        
        // Hide ID column
        tabelSoal.getColumnModel().getColumn(0).setMinWidth(0);
        tabelSoal.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelSoal.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        setupTableRenderers();

        // Double click to edit
        tabelSoal.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedRow();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelSoal);
        
        // Wrap content
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void setupTableRenderers() {
        TableCellRenderer imageRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = new JLabel();
                lbl.setHorizontalAlignment(JLabel.CENTER);
                if (value instanceof ImageIcon) {
                    lbl.setIcon((ImageIcon) value);
                }
                return lbl;
            }
        };

        int[] imageCols = {2};
        for (int col : imageCols) {
            tabelSoal.getColumnModel().getColumn(col).setCellRenderer(imageRenderer);
        }
        
        tabelSoal.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());
        tabelSoal.getColumnModel().getColumn(3).setCellRenderer(new MultiLineCellRenderer());
        tabelSoal.getColumnModel().getColumn(5).setCellRenderer(new MultiLineCellRenderer());
        tabelSoal.getColumnModel().getColumn(7).setCellRenderer(new MultiLineCellRenderer());
        tabelSoal.getColumnModel().getColumn(8).setCellRenderer(new MultiLineCellRenderer());
        tabelSoal.getColumnModel().getColumn(9).setCellRenderer(new MultiLineCellRenderer());
        tabelSoal.getColumnModel().getColumn(10).setCellRenderer(new MultiLineCellRenderer());
        
         // Set preferred widths
        TableColumnModel columnModel = tabelSoal.getColumnModel();
         for (int i = 1; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(150);
        }
    }

    private void getSoal() {
        tableModel.setRowCount(0); // Clear data
        
        // Load in background
        new SwingWorker<List<QuestionManipulation>, Void>() {
            @Override
            protected List<QuestionManipulation> doInBackground() {
               return question_repo.getAllQuestion(SoalForm.this);
            }

            @Override
            protected void done() {
                try {
                    List<QuestionManipulation> questions = get();
                     for (QuestionManipulation q : questions) {
                        StringBuilder correctAnswer = new StringBuilder();
                        String ansA="", ansB="", ansC="", ansD="";
                        
                        // Map answers fixed to 4 columns if possible
                        // Assuming logic matches TambahSoalForm (A, B, C, D order)
                         for (int i=0; i < q.answers.size(); i++) {
                             com.tugas.quizmath_player.entity.Answer a = q.answers.get(i);
                             if (a.correct) correctAnswer.append(a.label).append(", ");
                             
                             if (i==0) { ansA = a.answer; }
                             else if (i==1) { ansB = a.answer; }
                             else if (i==2) { ansC = a.answer; }
                             else if (i==3) { ansD = a.answer; }
                         }
                        
                         Object[] row = {
                            q.id,
                            q.question_text,
                            resizeImage(q.question_image, 100, 80),
                            q.answer_type,
                            q.level,
                            q.topic,
                            correctAnswer.toString(),
                             ansA,
                            ansB,
                            ansC,
                            ansD,
                        };
                        tableModel.addRow(row);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(SoalForm.this, "Error loading data: " + e.getMessage());
                }
            }
        }.execute();
    }
    
    // Helper resize
    private ImageIcon resizeImage(String path, int width, int height) {
        if (path == null || path.isEmpty()) return null;
        if (!new java.io.File(path).exists()) return null;
        try {
             ImageIcon icon = new ImageIcon(path);
             Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
             return new ImageIcon(img);
        } catch(Exception e) {
            return null;
        }
    }

    private void searchTable() {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + txtSearch.getText().trim()));
    }
    
    private void openTambahSoal(int id) {
        TambahSoalForm form = (id == 0) ? new TambahSoalForm() : new TambahSoalForm(id);
        form.setVisible(true);
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                getSoal(); // Refresh when closed
            }
        });
    }

    private void editSelectedRow() {
        int row = tabelSoal.getSelectedRow();
        if (row != -1) {
            int modelRow = tabelSoal.convertRowIndexToModel(row);
            int id = (int) tableModel.getValueAt(modelRow, 0); // Column 0 is ID
            openTambahSoal(id);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan diedit.");
        }
    }
}
