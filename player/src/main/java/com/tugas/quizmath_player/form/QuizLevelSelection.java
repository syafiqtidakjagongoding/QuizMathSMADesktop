package com.tugas.quizmath_player.form;

import javax.swing.*;

import com.tugas.quizmath_player.entity.Answer;
import com.tugas.quizmath_player.entity.QuestionManipulation;
import com.tugas.quizmath_player.repository.QuestionRepository;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class QuizLevelSelection extends JFrame {
    private QuestionRepository question_repo;

    public QuizLevelSelection() {
        setTitle("Quiz Game - Pilih Level");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        this.question_repo = new QuestionRepository();
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

        // Label judul
        JLabel titleLabel = new JLabel("Silahkan pilih level anda");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 80, 0);
        mainPanel.add(titleLabel, gbc);

        // Panel untuk tombol-tombol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 3, 30, 0));

        // Tombol MUDAH
        JButton btnMudah = createLevelButton("MUDAH", new Color(46, 204, 113), new Color(39, 174, 96));
        btnMudah.addActionListener(e -> {
            QuizQuestion qq;
            List<QuizQuestion> quizQuestions = new ArrayList<>();
            try {
                // Ambil data dari repository
                List<QuestionManipulation> dbQuestions = question_repo.getAllQuestion(this);

                // Filter berdasarkan level jika perlu
                dbQuestions = dbQuestions.stream()
                        .filter(q -> q.level.equalsIgnoreCase("MUDAH"))
                        .collect(Collectors.toList());

                // Convert dari QuestionManipulation ke QuizQuestion
                for (QuestionManipulation qm : dbQuestions) {
                    // Pastikan ada 4 jawaban
                    if (qm.answers == null || qm.answers.size() < 4) {
                        continue; // Skip soal yang tidak lengkap
                    }

                    // Ambil 4 jawaban pertama
                    String[] answerTexts = new String[4];
                    boolean[] correctAnswers = new boolean[4];
                    String[] answerImages = new String[4];

                    for (int i = 0; i < 4 && i < qm.answers.size(); i++) {
                        Answer ans = qm.answers.get(i);
                        answerTexts[i] = ans.answer;
                        correctAnswers[i] = ans.correct;
                        answerImages[i] = ans.image_answer;
                    }

                    // Buat QuizQuestion
                    qq = new QuizQuestion(
                            qm.question_text,
                            answerTexts,
                            correctAnswers,
                            qm.answer_type,
                            qm.question_image,
                            answerImages);

                    quizQuestions.add(qq);
                }

                // Shuffle questions
                Collections.shuffle(quizQuestions);

            } catch (Exception er) {
                er.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Gagal memuat soal dari database: " + er.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            QuizQuestionForm2 quiz_question = new QuizQuestionForm2(quizQuestions);
            quiz_question.setLevel("MUDAH");
            quiz_question.setVisible(true);
            this.dispose();

        });

        // Tombol NORMAL
        JButton btnNormal = createLevelButton("NORMAL", new Color(241, 196, 15), new Color(243, 156, 18));
        btnNormal.addActionListener(e -> {
            QuizQuestion qq;
            List<QuizQuestion> quizQuestions = new ArrayList<>();
            try {
                // Ambil data dari repository
                List<QuestionManipulation> dbQuestions = question_repo.getAllQuestion(this);

                // Filter berdasarkan level jika perlu
                dbQuestions = dbQuestions.stream()
                        .filter(q -> q.level.equalsIgnoreCase("NORMAL"))
                        .collect(Collectors.toList());

                // Convert dari QuestionManipulation ke QuizQuestion
                for (QuestionManipulation qm : dbQuestions) {
                    // Pastikan ada 4 jawaban
                    if (qm.answers == null || qm.answers.size() < 4) {
                        continue; // Skip soal yang tidak lengkap
                    }

                    // Ambil 4 jawaban pertama
                    String[] answerTexts = new String[4];
                    boolean[] correctAnswers = new boolean[4];
                    String[] answerImages = new String[4];

                    for (int i = 0; i < 4 && i < qm.answers.size(); i++) {
                        Answer ans = qm.answers.get(i);
                        answerTexts[i] = ans.answer;
                        correctAnswers[i] = ans.correct;
                        answerImages[i] = ans.image_answer;
                    }

                    // Buat QuizQuestion
                    qq = new QuizQuestion(
                            qm.question_text,
                            answerTexts,
                            correctAnswers,
                            qm.answer_type,
                            qm.question_image,
                            answerImages);

                    quizQuestions.add(qq);
                }

                // Shuffle questions
                Collections.shuffle(quizQuestions);

            } catch (Exception er) {
                er.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Gagal memuat soal dari database: " + er.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            QuizQuestionForm2 quiz_question = new QuizQuestionForm2(quizQuestions);
           
            quiz_question.setLevel("NORMAL");
            quiz_question.setVisible(true);
            this.dispose();

        });

        // Tombol SUSAH
        JButton btnSusah = createLevelButton("SUSAH", new Color(231, 76, 60), new Color(192, 57, 43));
        btnSusah.addActionListener(e -> {
            QuizQuestion qq;
            List<QuizQuestion> quizQuestions = new ArrayList<>();
            try {
                // Ambil data dari repository
                List<QuestionManipulation> dbQuestions = question_repo.getAllQuestion(this);

                // Filter berdasarkan level jika perlu
                dbQuestions = dbQuestions.stream()
                        .filter(q -> q.level.equalsIgnoreCase("SUSAH"))
                        .collect(Collectors.toList());

                // Convert dari QuestionManipulation ke QuizQuestion
                for (QuestionManipulation qm : dbQuestions) {
                    // Pastikan ada 4 jawaban
                    if (qm.answers == null || qm.answers.size() < 4) {
                        continue; // Skip soal yang tidak lengkap
                    }

                    // Ambil 4 jawaban pertama
                    String[] answerTexts = new String[4];
                    boolean[] correctAnswers = new boolean[4];
                    String[] answerImages = new String[4];

                    for (int i = 0; i < 4 && i < qm.answers.size(); i++) {
                        Answer ans = qm.answers.get(i);
                        answerTexts[i] = ans.answer;
                        correctAnswers[i] = ans.correct;
                        answerImages[i] = ans.image_answer;
                    }

                    // Buat QuizQuestion
                   qq = new QuizQuestion(
                            qm.question_text,
                            answerTexts,
                            correctAnswers,
                            qm.answer_type,
                            qm.question_image,
                            answerImages);

                    quizQuestions.add(qq);
                }

                // Shuffle questions
                Collections.shuffle(quizQuestions);

            } catch (Exception er) {
                er.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Gagal memuat soal dari database: " + er.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            QuizQuestionForm2 quiz_question = new QuizQuestionForm2(quizQuestions);
            quiz_question.setLevel("SUSAH");
            quiz_question.setVisible(true);
            this.dispose();

        });

        buttonPanel.add(btnMudah);
        buttonPanel.add(btnNormal);
        buttonPanel.add(btnSusah);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 50, 0, 50);
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createLevelButton(String text, Color color1, Color color2) {
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

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Gambar teks
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 36));
        button.setPreferredSize(new Dimension(300, 200));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizLevelSelection());
    }
}
