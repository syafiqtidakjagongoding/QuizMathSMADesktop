package com.tugas.quizmath_player.form;

import javax.swing.*;
import javax.swing.border.*;
import com.tugas.quizmath_player.repository.QuestionRepository;
import com.tugas.quizmath_player.entity.Answer;
import com.tugas.quizmath_player.entity.QuestionManipulation;
import com.tugas.quizmath_player.helper.Session;
import com.tugas.quizmath_player.repository.FinalScoreRepository;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuizQuestionForm2 extends JFrame {
	private JLabel timerLabel;
	private JLabel levelLabel;
	private JLabel questionNumberLabel;
	private JTextArea questionArea;
	private JLabel questionImageLabel;
	private QuestionRepository question_repo;

	private JCheckBox[] answerCheckBoxes;
	private JLabel[] answerLabels;
	private JButton btnPrevious, btnNext, btnSubmit;
	private Timer globalTimer;
	private int totalTimeLeft = 600; // 10 menit dalam detik
	private JProgressBar timeProgress;
	private FinalScoreRepository fscore_repo;

	// Data quiz
	private String currentLevel = "MUDAH";
	private int currentQuestionIndex = 0;
	private List<QuizQuestion> questions;
	private List<Integer> userAnswers; // Menyimpan jawaban user untuk setiap soal

	public QuizQuestionForm2(List<QuizQuestion> questions) {
		this.question_repo = new QuestionRepository();
		initializeQuestions(questions); // Load dummy questions
		this.fscore_repo = new FinalScoreRepository();
		setTitle("Quiz Game - Pertanyaan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.questions = questions;
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
		mainPanel.setLayout(new BorderLayout(20, 20));
		mainPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

		// Panel atas untuk info quiz
		JPanel topInfoPanel = new JPanel(new BorderLayout());
		topInfoPanel.setOpaque(false);

		// Panel kiri untuk level
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftPanel.setOpaque(false);
		levelLabel = new JLabel("Level: " + currentLevel);
		levelLabel.setFont(new Font("Arial", Font.BOLD, 24));
		levelLabel.setForeground(Color.WHITE);
		leftPanel.add(levelLabel);

		// Panel tengah untuk timer
		JPanel centerTimerPanel = new JPanel();
		centerTimerPanel.setLayout(new BoxLayout(centerTimerPanel, BoxLayout.Y_AXIS));
		centerTimerPanel.setOpaque(false);

		timerLabel = new JLabel("Waktu: 10:00");
		timerLabel.setFont(new Font("Arial", Font.BOLD, 32));
		timerLabel.setForeground(Color.WHITE);
		timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		timeProgress = new JProgressBar(0, 600);
		timeProgress.setValue(600);
		timeProgress.setPreferredSize(new Dimension(300, 20));
		timeProgress.setMaximumSize(new Dimension(300, 20));
		timeProgress.setAlignmentX(Component.CENTER_ALIGNMENT);

		centerTimerPanel.add(timerLabel);
		centerTimerPanel.add(Box.createVerticalStrut(5));
		centerTimerPanel.add(timeProgress);

		// Panel kanan untuk nomor soal
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.setOpaque(false);
		questionNumberLabel = new JLabel("Soal: 1/" + questions.size());
		questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 24));
		questionNumberLabel.setForeground(Color.WHITE);
		rightPanel.add(questionNumberLabel);

		topInfoPanel.add(leftPanel, BorderLayout.WEST);
		topInfoPanel.add(centerTimerPanel, BorderLayout.CENTER);
		topInfoPanel.add(rightPanel, BorderLayout.EAST);

		// Panel tengah untuk pertanyaan dan jawaban
		JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
		centerPanel.setOpaque(false);

		// Box pertanyaan
		JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
		questionPanel.setBackground(Color.WHITE);
		questionPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(52, 73, 94), 3),
				new EmptyBorder(20, 20, 20, 20)));

		// Panel untuk gambar pertanyaan (jika ada)
		questionImageLabel = new JLabel();
		questionImageLabel.setHorizontalAlignment(JLabel.CENTER);
		questionImageLabel.setPreferredSize(new Dimension(200, 150));
		questionImageLabel.setMaximumSize(new Dimension(200, 150));

		questionArea = new JTextArea();
		questionArea.setFont(new Font("Arial", Font.PLAIN, 22));
		questionArea.setLineWrap(true);
		questionArea.setWrapStyleWord(true);
		questionArea.setEditable(false);
		questionArea.setBackground(Color.WHITE);
		questionArea.setFocusable(false);

		JScrollPane questionScroll = new JScrollPane(questionArea);
		questionScroll.setBorder(null);
		questionScroll.setPreferredSize(new Dimension(600, 120));

		JPanel questionContentPanel = new JPanel(new BorderLayout(10, 0));
		questionContentPanel.setOpaque(false);
		questionContentPanel.add(questionImageLabel, BorderLayout.WEST);
		questionContentPanel.add(questionScroll, BorderLayout.CENTER);

		questionPanel.add(questionContentPanel, BorderLayout.CENTER);

		// Panel untuk 4 pilihan jawaban (2x2 grid)
		JPanel answersPanel = new JPanel(new GridLayout(2, 2, 20, 20));
		answersPanel.setOpaque(false);

		answerCheckBoxes = new JCheckBox[4];
		answerLabels = new JLabel[4];
		String[] options = { "A", "B", "C", "D" };

		for (int i = 0; i < 4; i++) {
			final int index = i;
			JPanel answerPanel = new JPanel(new BorderLayout(5, 5));
			answerPanel.setBackground(Color.WHITE);
			answerPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
					new EmptyBorder(10, 10, 10, 10)));

			answerCheckBoxes[i] = new JCheckBox(options[i]);
			answerCheckBoxes[i].setFont(new Font("Arial", Font.BOLD, 18));
			answerCheckBoxes[i].setForeground(new Color(52, 73, 94));
			answerCheckBoxes[i].setOpaque(false);
			answerCheckBoxes[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

			answerLabels[i] = new JLabel("", SwingConstants.CENTER);
			answerLabels[i].setFont(new Font("Arial", Font.PLAIN, 16));
			answerLabels[i].setForeground(new Color(52, 73, 94));

			answerPanel.add(answerCheckBoxes[i], BorderLayout.NORTH);
			answerPanel.add(answerLabels[i], BorderLayout.CENTER);

			// Hover effect
			final JPanel finalPanel = answerPanel;
			answerPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (!answerCheckBoxes[index].isSelected()) {
						finalPanel.setBackground(new Color(236, 240, 241));
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (!answerCheckBoxes[index].isSelected()) {
						finalPanel.setBackground(Color.WHITE);
					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					answerCheckBoxes[index].setSelected(!answerCheckBoxes[index].isSelected());
					updateAnswerSelection(index);
				}
			});

			answerCheckBoxes[i].addActionListener(e -> updateAnswerSelection(index));

			answersPanel.add(answerPanel);
		}

		centerPanel.add(questionPanel, BorderLayout.NORTH);
		centerPanel.add(answersPanel, BorderLayout.CENTER);

		// Panel navigasi di bawah
		JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		navigationPanel.setOpaque(false);

		btnPrevious = createNavigationButton("◄ PREVIOUS");
		btnPrevious.addActionListener(e -> previousQuestion());

		btnNext = createNavigationButton("NEXT ►");
		btnNext.addActionListener(e -> nextQuestion());

		btnSubmit = createNavigationButton("SUBMIT");
		btnSubmit.setVisible(false);
		btnSubmit.addActionListener(e -> submitQuiz());

		navigationPanel.add(btnPrevious);
		navigationPanel.add(btnNext);
		navigationPanel.add(btnSubmit);

		// Tambahkan semua panel ke main panel
		mainPanel.add(topInfoPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(navigationPanel, BorderLayout.SOUTH);

		add(mainPanel);

		// Load pertanyaan pertama
		loadQuestion();

		// Mulai timer
		startTimer();

		setVisible(true);
	}

	private void initializeQuestions(List<QuizQuestion> qstions) {
		questions = qstions;
		userAnswers = new ArrayList<>();

		// Initialize user answers dengan 0 (belum dijawab)
		for (int i = 0; i < questions.size(); i++) {
			userAnswers.add(0); // 0 = belum ada yang dipilih
		}
	}

	// Method untuk load questions - sementara pakai dummy data
	private List<QuizQuestion> loadFromDatabase() {
		// List<QuizQuestion> dummyQuestions = new ArrayList<>();

		// // Soal 1 - Single Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Apa ibu kota Indonesia?",
		// new String[] { "Jakarta", "Bandung", "Surabaya", "Medan" },
		// new boolean[] { true, false, false, false },
		// "SINGLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// // Soal 2 - Multiple Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Pilih bilangan prima berikut: (Bisa lebih dari 1)",
		// new String[] { "2", "4", "7", "9" },
		// new boolean[] { true, false, true, false },
		// "MULTIPLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// // Soal 3 - Single Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Berapakah hasil dari 5 + 3 = ?",
		// new String[] { "6", "7", "8", "9" },
		// new boolean[] { false, false, true, false },
		// "SINGLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// // Soal 4 - Single Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Planet terbesar di tata surya adalah?",
		// new String[] { "Mars", "Jupiter", "Saturnus", "Neptunus" },
		// new boolean[] { false, true, false, false },
		// "SINGLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// // Soal 5 - Multiple Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Pilih bahasa pemrograman berikut: (Bisa lebih dari 1)",
		// new String[] { "Java", "HTML", "Python", "CSS" },
		// new boolean[] { true, false, true, false },
		// "MULTIPLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// // Soal 6 - Single Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Siapa presiden pertama Indonesia?",
		// new String[] { "Soekarno", "Soeharto", "Habibie", "Megawati" },
		// new boolean[] { true, false, false, false },
		// "SINGLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// // Soal 7 - Single Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Berapakah hasil dari 12 x 12 = ?",
		// new String[] { "124", "134", "144", "154" },
		// new boolean[] { false, false, true, false },
		// "SINGLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// // Soal 8 - Multiple Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Pilih negara di Asia Tenggara: (Bisa lebih dari 1)",
		// new String[] { "Thailand", "Jepang", "Vietnam", "Korea" },
		// new boolean[] { true, false, true, false },
		// "MULTIPLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// // Soal 9 - Single Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Apa simbol kimia untuk air?",
		// new String[] { "H2O", "CO2", "O2", "N2" },
		// new boolean[] { true, false, false, false },
		// "SINGLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// // Soal 10 - Single Choice
		// dummyQuestions.add(new QuizQuestion(
		// "Tahun kemerdekaan Indonesia adalah?",
		// new String[] { "1942", "1943", "1944", "1945" },
		// new boolean[] { false, false, false, true },
		// "SINGLE_CHOICES",
		// null,
		// new String[] { null, null, null, null }));

		// TODO: Nanti ganti dengan query ke database
		// Contoh:
		List<QuizQuestion> quizQuestions = new ArrayList<>();
		try {
			// Ambil data dari repository
			List<QuestionManipulation> dbQuestions = question_repo.getAllQuestion(this);

			// Filter berdasarkan level jika perlu
			dbQuestions = dbQuestions.stream()
					.filter(q -> q.level.equalsIgnoreCase(currentLevel))
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
				QuizQuestion qq = new QuizQuestion(
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

			return quizQuestions;

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"Gagal memuat soal dari database: " + e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
		// return dummyQuestions;
		return quizQuestions;
	}

	private void loadQuestion() {
		if (currentQuestionIndex < 0 || currentQuestionIndex >= questions.size()) {
			return;
		}

		QuizQuestion q = questions.get(currentQuestionIndex);

		// Update nomor soal
		questionNumberLabel.setText("Soal: " + (currentQuestionIndex + 1) + "/" + questions.size());

		// Set pertanyaan
		questionArea.setText(q.questionText);

		// Set gambar pertanyaan jika ada
		if (q.questionImagePath != null && !q.questionImagePath.isEmpty()) {
			ImageIcon icon = new ImageIcon(q.questionImagePath);
			Image scaled = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
			questionImageLabel.setIcon(new ImageIcon(scaled));
			questionImageLabel.setVisible(true);
		} else {
			questionImageLabel.setIcon(null);
			questionImageLabel.setVisible(false);
		}

		// Set jawaban
		for (int i = 0; i < 4; i++) {
			if (q.answerImagePaths[i] != null && !q.answerImagePaths[i].isEmpty()) {
				// Jika ada gambar
				ImageIcon icon = new ImageIcon(q.answerImagePaths[i]);
				Image scaled = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
				answerLabels[i].setIcon(new ImageIcon(scaled));
				answerLabels[i].setText("");
			} else {
				// Jika hanya teks
				answerLabels[i].setIcon(null);
				answerLabels[i].setText("<html><div style='text-align: center; width: 200px;'>" +
						q.answers[i] + "</div></html>");
			}

			// Reset checkbox
			answerCheckBoxes[i].setSelected(false);
		}

		// Load jawaban user sebelumnya jika ada
		int savedAnswer = userAnswers.get(currentQuestionIndex);
		if (savedAnswer > 0) {
			for (int i = 0; i < 4; i++) {
				if ((savedAnswer & (1 << i)) != 0) {
					answerCheckBoxes[i].setSelected(true);
				}
			}
		}

		// Setup mode (single atau multiple choice)
		setupAnswerMode(q.answerType);

		// Update visibility tombol
		btnPrevious.setVisible(currentQuestionIndex > 0);

		if (currentQuestionIndex == questions.size() - 1) {
			btnNext.setVisible(false);
			btnSubmit.setVisible(true);
		} else {
			btnNext.setVisible(true);
			btnSubmit.setVisible(false);
		}
	}

	private void setupAnswerMode(String answerType) {
		if ("SINGLE_CHOICES".equalsIgnoreCase(answerType)) {
			// Mode single choice - hanya bisa pilih 1
			for (int i = 0; i < answerCheckBoxes.length; i++) {
				final int index = i;
				// Remove all action listeners
				for (ActionListener al : answerCheckBoxes[i].getActionListeners()) {
					answerCheckBoxes[i].removeActionListener(al);
				}
				// Add new listener untuk single choice
				answerCheckBoxes[i].addActionListener(e -> {
					if (answerCheckBoxes[index].isSelected()) {
						// Uncheck yang lain
						for (int j = 0; j < answerCheckBoxes.length; j++) {
							if (j != index) {
								answerCheckBoxes[j].setSelected(false);
							}
						}
					}
					saveCurrentAnswer();
				});
			}
		} else {
			// Mode multiple choice - bisa pilih banyak
			for (int i = 0; i < answerCheckBoxes.length; i++) {
				// Remove all action listeners
				for (ActionListener al : answerCheckBoxes[i].getActionListeners()) {
					answerCheckBoxes[i].removeActionListener(al);
				}
				// Add simple listener
				answerCheckBoxes[i].addActionListener(e -> saveCurrentAnswer());
			}
		}
	}

	private void updateAnswerSelection(int index) {
		QuizQuestion q = questions.get(currentQuestionIndex);

		if ("SINGLE_CHOICES".equalsIgnoreCase(q.answerType)) {
			// Single choice - uncheck yang lain
			if (answerCheckBoxes[index].isSelected()) {
				for (int j = 0; j < answerCheckBoxes.length; j++) {
					if (j != index) {
						answerCheckBoxes[j].setSelected(false);
					}
				}
			}
		}

		saveCurrentAnswer();
	}

	private void saveCurrentAnswer() {
		int answer = 0;
		for (int i = 0; i < answerCheckBoxes.length; i++) {
			if (answerCheckBoxes[i].isSelected()) {
				answer |= (1 << i); // Set bit ke-i
			}
		}
		userAnswers.set(currentQuestionIndex, answer);
	}

	private JButton createNavigationButton(String text) {
		JButton button = new JButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

				Color color1 = new Color(46, 204, 113);
				Color color2 = new Color(39, 174, 96);

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

		button.setFont(new Font("Arial", Font.BOLD, 18));
		button.setPreferredSize(new Dimension(180, 50));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		return button;
	}

	private void startTimer() {
		globalTimer = new Timer(1000, e -> {
			totalTimeLeft--;
			timeProgress.setValue(totalTimeLeft);

			int minutes = totalTimeLeft / 60;
			int seconds = totalTimeLeft % 60;
			timerLabel.setText(String.format("Waktu: %02d:%02d", minutes, seconds));

			// Ubah warna timer jika waktu hampir habis
			if (totalTimeLeft <= 60) {
				timerLabel.setForeground(new Color(231, 76, 60));
			} else if (totalTimeLeft <= 180) {
				timerLabel.setForeground(new Color(241, 196, 15));
			}

			if (totalTimeLeft <= 0) {
				globalTimer.stop();
				JOptionPane.showMessageDialog(this, "Waktu habis!", "Time's Up", JOptionPane.WARNING_MESSAGE);
				submitQuiz();
			}
		});
		globalTimer.start();
	}

	private void previousQuestion() {
		if (currentQuestionIndex > 0) {
			saveCurrentAnswer();
			currentQuestionIndex--;
			loadQuestion();
		}
	}

	private void nextQuestion() {
		// Cek apakah user sudah menjawab
		boolean answered = false;
		for (JCheckBox cb : answerCheckBoxes) {
			if (cb.isSelected()) {
				answered = true;
				break;
			}
		}

		if (!answered) {
			int option = JOptionPane.showConfirmDialog(this,
					"Anda belum menjawab soal ini. Lanjut ke soal berikutnya?",
					"Peringatan",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE);

			if (option != JOptionPane.YES_OPTION) {
				return;
			}
		}

		if (currentQuestionIndex < questions.size() - 1) {
			saveCurrentAnswer();
			currentQuestionIndex++;
			loadQuestion();
		}
	}

	private void submitQuiz() {
		saveCurrentAnswer();

		int option = JOptionPane.showConfirmDialog(this,
				"Apakah Anda yakin ingin submit quiz?",
				"Konfirmasi Submit",
				JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.YES_OPTION) {
			globalTimer.stop();

			// Hitung jawaban benar
			int correctAnswers = 0;
			for (int i = 0; i < questions.size(); i++) {
				QuizQuestion q = questions.get(i);
				int userAnswer = userAnswers.get(i);

				// Cek jawaban benar
				int correctAnswer = 0;
				for (int j = 0; j < 4; j++) {
					if (q.correctAnswers[j]) {
						correctAnswer |= (1 << j);
					}
				}

				if (userAnswer == correctAnswer) {
					correctAnswers++;
				}
			}

			int siswaId = Session.getCurrentSiswaId();

			this.fscore_repo.insertScore(rootPane, siswaId, correctAnswers, questions.size());
			// Tampilkan hasil
			new QuizResultForm(currentLevel, questions.size(), correctAnswers).setVisible(true);
			this.dispose();
		}
	}

	// Method untuk set level dan total waktu dari luar
	public void setLevel(String level) {
		this.currentLevel = level;
		levelLabel.setText("Level: " + level);
	}

	public void setTotalTime(int seconds) {
		this.totalTimeLeft = seconds;
		timeProgress.setMaximum(seconds);
		timeProgress.setValue(seconds);
	}

	public void setQuestions(List<QuizQuestion> questions) {
		this.questions = questions;
		this.userAnswers = new ArrayList<>();
		for (int i = 0; i < questions.size(); i++) {
			userAnswers.add(0);
		}
		Collections.shuffle(this.questions);
		loadQuestion();
	}

}

// Class untuk menyimpan data pertanyaan
class QuizQuestion {
	String questionText;
	String[] answers;
	boolean[] correctAnswers;
	String answerType; // "SINGLE_CHOICES" atau "MULTIPLE_CHOICES"
	String questionImagePath;
	String[] answerImagePaths;

	public QuizQuestion(String questionText, String[] answers, boolean[] correctAnswers,
			String answerType, String questionImagePath, String[] answerImagePaths) {
		this.questionText = questionText;
		this.answers = answers;
		this.correctAnswers = correctAnswers;
		this.answerType = answerType;
		this.questionImagePath = questionImagePath;
		this.answerImagePaths = answerImagePaths;
	}
}