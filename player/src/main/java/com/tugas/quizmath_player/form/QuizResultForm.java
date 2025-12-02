package com.tugas.quizmath_player.form;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class QuizResultForm extends JFrame {
	private int wrongAnswers;
	private int score;

	public QuizResultForm(String level, int totalQuestions, int correctAnswers) {
		this.wrongAnswers = totalQuestions - correctAnswers;
		this.score = (int) ((correctAnswers * 100.0) / totalQuestions);

		setTitle("Quiz Game - Hasil");
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

		// Panel hasil dengan background putih
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		resultPanel.setBackground(Color.WHITE);
		resultPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(52, 73, 94), 4),
				new EmptyBorder(50, 80, 50, 80)));

		// Judul
		JLabel titleLabel = new JLabel("HASIL QUIZ");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
		titleLabel.setForeground(new Color(52, 73, 94));
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Level
		JLabel levelLabel = new JLabel("Level: " + level);
		levelLabel.setFont(new Font("Arial", Font.BOLD, 28));
		levelLabel.setForeground(new Color(52, 73, 94));
		levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Spacing
		resultPanel.add(Box.createVerticalStrut(20));
		resultPanel.add(titleLabel);
		resultPanel.add(Box.createVerticalStrut(10));
		resultPanel.add(levelLabel);
		resultPanel.add(Box.createVerticalStrut(40));

		// Score besar dengan lingkaran
		JPanel scorePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Tentukan warna berdasarkan score
				Color scoreColor;
				if (score >= 80) {
					scoreColor = new Color(46, 204, 113); // Hijau
				} else if (score >= 60) {
					scoreColor = new Color(241, 196, 15); // Kuning
				} else {
					scoreColor = new Color(231, 76, 60); // Merah
				}

				// Gambar lingkaran
				int diameter = 200;
				int x = (getWidth() - diameter) / 2;
				int y = (getHeight() - diameter) / 2;

				// Shadow
				g2d.setColor(new Color(0, 0, 0, 30));
				g2d.fillOval(x + 5, y + 5, diameter, diameter);

				// Lingkaran utama
				g2d.setColor(scoreColor);
				g2d.fillOval(x, y, diameter, diameter);

				// Teks score
				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("Arial", Font.BOLD, 72));
				String scoreText = score + "%";
				FontMetrics fm = g2d.getFontMetrics();
				int textX = (getWidth() - fm.stringWidth(scoreText)) / 2;
				int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
				g2d.drawString(scoreText, textX, textY);
			}
		};
		scorePanel.setPreferredSize(new Dimension(250, 250));
		scorePanel.setMaximumSize(new Dimension(250, 250));
		scorePanel.setOpaque(false);
		scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		resultPanel.add(scorePanel);
		resultPanel.add(Box.createVerticalStrut(40));

		// Detail hasil
		JPanel detailsPanel = new JPanel();
		detailsPanel.setLayout(new GridLayout(3, 2, 20, 15));
		detailsPanel.setOpaque(false);
		detailsPanel.setMaximumSize(new Dimension(500, 150));

		// Total Questions
		JLabel lblTotalQ = new JLabel("Total Soal:");
		lblTotalQ.setFont(new Font("Arial", Font.BOLD, 22));
		lblTotalQ.setForeground(new Color(52, 73, 94));
		JLabel valTotalQ = new JLabel(String.valueOf(totalQuestions));
		valTotalQ.setFont(new Font("Arial", Font.PLAIN, 22));
		valTotalQ.setForeground(new Color(52, 73, 94));
		valTotalQ.setHorizontalAlignment(SwingConstants.RIGHT);

		// Correct Answers
		JLabel lblCorrect = new JLabel("Jawaban Benar:");
		lblCorrect.setFont(new Font("Arial", Font.BOLD, 22));
		lblCorrect.setForeground(new Color(46, 204, 113));
		JLabel valCorrect = new JLabel(String.valueOf(correctAnswers));
		valCorrect.setFont(new Font("Arial", Font.PLAIN, 22));
		valCorrect.setForeground(new Color(46, 204, 113));
		valCorrect.setHorizontalAlignment(SwingConstants.RIGHT);

		// Wrong Answers
		JLabel lblWrong = new JLabel("Jawaban Salah:");
		lblWrong.setFont(new Font("Arial", Font.BOLD, 22));
		lblWrong.setForeground(new Color(231, 76, 60));
		JLabel valWrong = new JLabel(String.valueOf(wrongAnswers));
		valWrong.setFont(new Font("Arial", Font.PLAIN, 22));
		valWrong.setForeground(new Color(231, 76, 60));
		valWrong.setHorizontalAlignment(SwingConstants.RIGHT);

		detailsPanel.add(lblTotalQ);
		detailsPanel.add(valTotalQ);
		detailsPanel.add(lblCorrect);
		detailsPanel.add(valCorrect);
		detailsPanel.add(lblWrong);
		detailsPanel.add(valWrong);

		resultPanel.add(detailsPanel);
		resultPanel.add(Box.createVerticalStrut(40));

		// Pesan motivasi
		String message = getMotivationMessage(score);
		JLabel messageLabel = new JLabel(message);
		messageLabel.setFont(new Font("Arial", Font.ITALIC, 20));
		messageLabel.setForeground(new Color(127, 140, 141));
		messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		resultPanel.add(messageLabel);
		resultPanel.add(Box.createVerticalStrut(40));

		// Tombol aksi
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		buttonPanel.setOpaque(false);
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton btnPlayAgain = createActionButton("MAIN LAGI", new Color(46, 204, 113), new Color(39, 174, 96));
		btnPlayAgain.addActionListener(e -> playAgain());

		JButton btnExit = createActionButton("KELUAR", new Color(231, 76, 60), new Color(192, 57, 43));
		btnExit.addActionListener(e -> {
			new LoginForm().setVisible(true);
			this.dispose();
		}
	);

		buttonPanel.add(btnPlayAgain);
		buttonPanel.add(btnExit);

		resultPanel.add(buttonPanel);

		// Tambahkan result panel ke main panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		mainPanel.add(resultPanel, gbc);

		add(mainPanel);
		setVisible(true);
	}

	private String getMotivationMessage(int score) {
		if (score >= 90) {
			return "Sempurna! Kamu luar biasa!";
		} else if (score >= 80) {
			return "Bagus sekali! Pertahankan!";
		} else if (score >= 70) {
			return "Lumayan! Masih bisa lebih baik!";
		} else if (score >= 60) {
			return "Cukup baik, terus berlatih!";
		} else {
			return "Jangan menyerah, coba lagi!";
		}
	}

	private JButton createActionButton(String text, Color color1, Color color2) {
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
		button.setPreferredSize(new Dimension(200, 55));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		return button;
	}

	private void playAgain() {
		// Tutup form ini dan buka form pemilihan level
		this.dispose();
		SwingUtilities.invokeLater(() -> new QuizLevelSelection());
	}
}
