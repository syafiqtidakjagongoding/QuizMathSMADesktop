package com.tugas.quizmath_player.form;

import com.tugas.quizmath_player.entity.Answer;
import com.tugas.quizmath_player.entity.Question;
import com.tugas.quizmath_player.entity.QuestionManipulation;
import com.tugas.quizmath_player.repository.QuestionRepository;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TambahSoalForm extends JFrame {

    private JTextArea txtQuestion;
    private JComboBox<String> cmbLevel;
    private JTextField txtTopic;
    private JLabel lblQuestionImage;
    private String questionImagePath;
    
    private List<AnswerPanel> answerPanels;
    private JButton btnSave;
    private JButton btnCancel;
    
    private QuestionRepository questionRepository;
    private boolean isEditMode = false;
    private int questionId = -1;

    public TambahSoalForm() {
        this.questionRepository = new QuestionRepository();
        initComponents();
    }

    public TambahSoalForm(int id) {
        this.isEditMode = true;
        this.questionId = id;
        this.questionRepository = new QuestionRepository();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle(isEditMode ? "Edit Soal" : "Tambah Soal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Main Panel (Scrollable)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Section 1: Question Header
        mainPanel.add(createQuestionSection());
        mainPanel.add(Box.createVerticalStrut(20));

        // Section 2: Answers
        mainPanel.add(createAnswerSection());
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton(isEditMode ? "Update" : "Simpan");
        btnSave.setBackground(new Color(34, 139, 34)); // Green
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> saveQuestion());

        btnCancel = new JButton("Batal");
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createQuestionSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Informasi Soal",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Question Text
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Pertanyaan:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.9;
        txtQuestion = new JTextArea(3, 40);
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        JScrollPane scrollQ = new JScrollPane(txtQuestion);
        panel.add(scrollQ, gbc);

        // Level
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        panel.add(new JLabel("Level:"), gbc);

        gbc.gridx = 1; 
        cmbLevel = new JComboBox<>(new String[]{"MUDAH", "NORMAL", "SUSAH"});
        panel.add(cmbLevel, gbc);

        // Topic
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Topik:"), gbc);

        gbc.gridx = 1; 
        txtTopic = new JTextField();
        panel.add(txtTopic, gbc);

        // Image
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Gambar Soal:"), gbc);

        gbc.gridx = 1;
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnUpload = new JButton("Upload Gambar");
        btnUpload.addActionListener(e -> uploadQuestionImage());
        JButton btnRemove = new JButton("Hapus Gambar");
        btnRemove.addActionListener(e -> {
            questionImagePath = null;
            lblQuestionImage.setIcon(null);
            lblQuestionImage.setText("No Image");
        });
        
        lblQuestionImage = new JLabel("No Image", SwingConstants.CENTER);
        lblQuestionImage.setPreferredSize(new Dimension(100, 100));
        lblQuestionImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        imagePanel.add(btnUpload);
        imagePanel.add(btnRemove);
        imagePanel.add(lblQuestionImage);
        panel.add(imagePanel, gbc);

        return panel;
    }

    private JPanel createAnswerSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Pilihan Jawaban (A-D)",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));

        answerPanels = new ArrayList<>();
        // Create 4 answer panels
        String[] labels = {"A", "B", "C", "D"};
        for (String label : labels) {
            AnswerPanel ap = new AnswerPanel(label);
            answerPanels.add(ap);
            panel.add(ap);
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }

    private void uploadQuestionImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            questionImagePath = f.getAbsolutePath();
            displayImage(lblQuestionImage, questionImagePath);
        }
    }

    private void displayImage(JLabel label, String path) {
        if (path != null && !path.isEmpty()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
            label.setText("");
        } else {
            label.setIcon(null);
            label.setText("No Image");
        }
    }

    private void loadData() {
        Question q = questionRepository.getQuestionById(questionId);
        if (q != null) {
            txtQuestion.setText(q.getQuestionText());
            cmbLevel.setSelectedItem(q.getLevel());
            txtTopic.setText(q.getTopic());
            questionImagePath = q.getImagePath(); // handled in repo
            displayImage(lblQuestionImage, questionImagePath);

            // Load answers
            // Note: Assuming answers are stored in order A, B, C, D or mapped by some logic.
            // Converting set to list might lose order if not handled. 
            // QuestionRepository.getAllQuestion returns List<Answer> in order, 
            // but here we have the Entity which has a Set or List.
            // Let's rely on getAllQuestion equivalent logic or just iterate.
            
            // To be safe and consistent with current SoalForm implementation,
            // we should re-fetch as DTO or manually handle list.
            // QuestionRepository currently populates DTO manually from entity list.
            // Let's iterate entity's answers.
            List<com.tugas.quizmath_player.entity.OptionAnswer> options = new ArrayList<>(q.getAnswers());
            
            // Basic mapping - assuming index 0=A, 1=B, etc. if size is 4
            for (int i = 0; i < Math.min(answerPanels.size(), options.size()); i++) {
                 com.tugas.quizmath_player.entity.OptionAnswer opt = options.get(i);
                 AnswerPanel ap = answerPanels.get(i);
                 ap.setAnswerText(opt.getAnswer());
                 ap.setCorrect(opt.isCorrect());
            }
        }
    }

    private void saveQuestion() {
        if (txtQuestion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pertanyaan harus diisi!");
            return;
        }

        // Validate at least one correct answer
        int correctCount = 0;
        for (AnswerPanel ap : answerPanels) {
            if (ap.isCorrect()) {
                correctCount++;
            }
        }

        if (correctCount == 0) {
            JOptionPane.showMessageDialog(this, "Centang minimal satu jawaban benar!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String answerType = (correctCount > 1) ? "MULTIPLE_CHOICES" : "SINGLE_CHOICES";

        List<Answer> newAnswers = new ArrayList<>();
        for (AnswerPanel ap : answerPanels) {
            newAnswers.add(new Answer(
                    0, // id placeholder
                    ap.getAnswerText(),
                    ap.getLabel(), 
                    0, // score placeholder
                    ap.isCorrect()
            ));
        }

        QuestionManipulation qm = new QuestionManipulation(
                isEditMode ? questionId : 0,
                txtQuestion.getText().trim(),
                questionImagePath,
                answerType,
                        (String) cmbLevel.getSelectedItem(),
                txtTopic.getText().trim(),
                newAnswers
        );

        if (isEditMode) {
            questionRepository.updateSoal(qm, this);
        } else {
            questionRepository.createSoal(qm, this);
        }

        dispose();
    }

    // Inner class for Answer row
    private class AnswerPanel extends JPanel {
        private String label;
        private JTextArea txtAnswer;
        private JCheckBox chkCorrect;

        public AnswerPanel(String label) {
            this.label = label;
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createEtchedBorder());
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Label (A, B, C, D)
            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.05;
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Arial", Font.BOLD, 18));
            add(lbl, gbc);

            // Text
            gbc.gridx = 1; gbc.weightx = 0.6;
            txtAnswer = new JTextArea(2, 20);
            txtAnswer.setLineWrap(true);
            add(new JScrollPane(txtAnswer), gbc);

            // Is Correct
            gbc.gridx = 2; gbc.weightx = 0.1;
            chkCorrect = new JCheckBox("Benar");
            add(chkCorrect, gbc);
        }

        public String getLabel() { return label; }
        public String getAnswerText() { return txtAnswer.getText().trim(); }

        public boolean isCorrect() {
            return chkCorrect.isSelected();
        }

        public void setAnswerText(String text) { txtAnswer.setText(text); }

        public void setCorrect(boolean correct) {
            chkCorrect.setSelected(correct);
        }
    }
}
